# Optimistic Entity
Optimistic entity is a concurrency model I thought of while reading actors and the single writer principle. The core idea of this model is to submit a proposal or batch proposal requesting changes to an entity.
</br> If a single proposal is submitted requesting changes is submitted with stale data, the proposal is immediately rejected, if not the entity is updated. The same goes for batch proposals, if a batch proposal is submitted with at least one stale value, the whole batch is rejected.
</br> This model is good for making sure an entity is updated with only fresh and up-to-date values. No retries allowed. That's the beauty of this model

## Creating an entity
Creating an optimistic entity is very simple. An entity can be created using the `Entities` factory class

```java
class Document {
    private int id;
    private String text;
    
    Document(int id){
        this.id = id;
    }
    
    //Getters, Setters
}

Entity<Document> documentEntity = Entities.spawnEntity(new Document(1));
```

## Submitting a proposal
A proposal is a change or a batch of changes you want applied to an entity. As stated before, if a proposal is submitted with stale `seen` values, it is immediately rejected

```java
Entity<Document> entity = Entities.spawnEntity(new Document(1));
Document snapshot = entity.snapshot();
Proposal<Document, String> docProposal = new Proposal<Document, String>()
        .builder()
        .getter(Document::getText)
        .setter(Document::setText)
        .seenValue(snapshot.getText())
        .proposedValue(snapshot.getText() + "My edit")
        .build();
    entity.propose(docProposal);
    Thread.sleep(10); //Give the entity some time to process the proposal
    IO.println(snapshot.getText()); //Proposal should be applied
```

## Submitting a stale proposal
Submitting a stale proposal should cause the proposal to be dropped
```java
Entity<Document> entity = Entities.spawnEntity(new Document(1));
Document snapshot = entity.snapshot();
String staleValue = "stale";
Proposal<Document, String> docProposal = new Proposal<Document, String>()
        .builder()
        .getter(Document::getText)
        .setter(Document::setText)
        .seenValue(staleValue)
        .proposedValue(staleValue + "My edit")
        .onSuccess(() -> IO.println(snapshot))
        .onReject(() -> IO.println(snapshot))
        .build();
    entity.propose(docProposal);
    Thread.sleep(10); //Give the entity some time to process the proposal
    IO.println(snapshot.getText()); //Proposal should be dropped
```

## Proposal metrics
You can get an entity's proposal metrics easily 
```java
Entity<Void> entity = Entities.spawnEntity(null);
long rejected = entity.rejectedCount();
List<List<Proposal<Void, ?>>>  rejectedProps = entity.rejectedProposals();
double rate = entity.rejectionRate();
```


## Versioning
You can get an entity's version, and it's state at that version
```java
Entity<Document> entity = Entities.spawnEntity(new Document(1));
Document d = entity.snapshot(); //Current entity state
Document d = entity.snaphotAt(1); //Get entity's state at v1
long v = entity.currentVersionNo();
```

## Invariants
- An entity cannot process more than one proposal at a time
- An entity's state must not be modified outside of it's given entity. Though this is hugely dependent on the developer's discipline
- A proposal with a stale seen value must always be rejected
- A dropped proposal must never be retried by an entity

## Use cases
Tbh I'm not too sure where this is useful, it was fun though icl





