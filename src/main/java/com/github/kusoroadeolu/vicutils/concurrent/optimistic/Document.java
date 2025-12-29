package com.github.kusoroadeolu.vicutils.concurrent.optimistic;

import java.util.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.BiFunction;

// Document state - just a string with a version number
class Document {
    private final String content;
    private final int version;

    public Document(String content, int version) {
        this.content = content;
        this.version = version;
    }

    public String getContent() { return content; }
    public int getVersion() { return version; }

    public Document applyEdit(Edit edit) {
        String newContent = switch (edit.type()) {
            case INSERT -> content.substring(0, edit.position()) +
                    edit.text() +
                    content.substring(edit.position());
            case DELETE -> content.substring(0, edit.position()) +
                    content.substring(edit.position() + edit.length());
            case REPLACE -> content.substring(0, edit.position()) +
                    edit.text() +
                    content.substring(edit.position() + edit.length());
        };
        return new Document(newContent, version + 1);
    }

    @Override
    public String toString() {
        return "v" + version + ": \"" + content + "\"";
    }
}

// Edit operations
enum EditType { INSERT, DELETE, REPLACE }

record Edit(EditType type, int position, String text, int length) {
    static Edit insert(int pos, String text) {
        return new Edit(EditType.INSERT, pos, text, 0);
    }

    static Edit delete(int pos, int length) {
        return new Edit(EditType.DELETE, pos, "", length);
    }

    static Edit replace(int pos, int length, String text) {
        return new Edit(EditType.REPLACE, pos, text, length);
    }
}

// Client that makes edits
class EditorClient {
    private final String name;
    private final OptimisticEntity<Document> documentEntity;
    private final Random random = new Random();
    private int successfulEdits = 0;
    private int failedEdits = 0;

    public EditorClient(String name, OptimisticEntity<Document> documentEntity) {
        this.name = name;
        this.documentEntity = documentEntity;
    }

    public void makeRandomEdit() {
        // Get current snapshot
        Document currentDoc = documentEntity.snapshot();
        int docLength = currentDoc.getContent().length();

        if (docLength == 0) return;

        // Create a random edit
        Edit edit;
        int editType = random.nextInt(3);
        int position = random.nextInt(docLength);

        edit = switch (editType) {
            case 0 -> Edit.insert(position, "[" + name + "]");
            case 1 -> {
                int len = Math.min(3, docLength - position);
                yield len > 0 ? Edit.delete(position, len) : null;
            }
            case 2 -> {
                int len = Math.min(2, docLength - position);
                yield len > 0 ? Edit.replace(position, len, "<" + name + ">") : null;
            }
            default -> null;
        };

        if (edit == null) return;

        // Create proposal
        Proposal<Document, Integer> proposal = new Proposal<Document, Integer>().builder()
                .getter(Document::getVersion)
                .seenValue(currentDoc.getVersion())
                .proposedValue(currentDoc.getVersion() + 1)
                .setter((doc, _) -> doc.applyEdit(edit))
                .build();

        // Track before submission
        long rejectedBefore = documentEntity.rejectedCount();
        documentEntity.propose(proposal);

        // Check if it was accepted
        long rejectedAfter = documentEntity.rejectedCount();
        if (rejectedAfter > rejectedBefore) {
            failedEdits++;
            System.out.println("  ❌ " + name + " edit REJECTED (stale version)");
        } else {
            successfulEdits++;
            System.out.println("  ✅ " + name + " applied: " + edit.type());
        }
    }

    public void printStats() {
        int total = successfulEdits + failedEdits;
        double successRate = total > 0 ? (100.0 * successfulEdits / total) : 0;
        System.out.printf("%s: %d/%d successful (%.1f%%)%n",
                name, successfulEdits, total, successRate);
    }
}

// Main simulation
class CollaborativeDocSimulation {
    public static void main(String[] args) throws InterruptedException {
        // Create document entity
        Document initialDoc = new Document("Hello World", 0);
        OptimisticEntity<Document> docEntity =
                (OptimisticEntity<Document>) Entities.spawnEntity(initialDoc);

        System.out.println("Initial document: " + docEntity.snapshot());
        System.out.println("\n=== Starting collaborative editing ===\n");

        // Create multiple clients
        List<EditorClient> clients = new ArrayList<>(List.of(
                new EditorClient("Alice", docEntity),
                new EditorClient("Bob", docEntity),
                new EditorClient("Carol", docEntity),
                new EditorClient("Dave", docEntity)
        ));

        // Run simulation - clients make random edits
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        int rounds = 20;

        for (int round = 0; round < rounds; round++) {
            System.out.println("\n--- Round " + (round + 1) + " ---");

            // All clients try to edit simultaneously
            CountDownLatch latch = new CountDownLatch(clients.size());
            Collections.shuffle(clients);
            for (EditorClient client : clients) {
                executor.submit(() -> {
                    try {
                        client.makeRandomEdit();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

            // Show current state
            Document current = docEntity.snapshot();
            System.out.println("\nCurrent doc: " + current);
            System.out.println("Content: \"" + current.getContent() + "\"");
        }

        // Print final stats
        System.out.println("\n\n=== Final Statistics ===\n");
        clients.forEach(EditorClient::printStats);

        System.out.printf("%nDocument Entity:%n");
        System.out.printf("  Accepted: %d%n", docEntity.acceptedCount());
        System.out.printf("  Rejected: %d%n", docEntity.rejectedCount());
        System.out.printf("  Rejection Rate: %.1f%%%n", docEntity.rejectionRate() * 100);

        System.out.println("\nFinal document: " + docEntity.snapshot());
        System.out.println("Final content: \"" + docEntity.snapshot().getContent() + "\"");

        docEntity.stop();
        executor.close();
    }
}