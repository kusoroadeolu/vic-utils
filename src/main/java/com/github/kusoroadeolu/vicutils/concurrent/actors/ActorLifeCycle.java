package com.github.kusoroadeolu.vicutils.concurrent.actors;

interface ActorLifeCycle {
    void start();
    void stop();
    void preStop();
    void preStart();
    void onChildRestart();
}
