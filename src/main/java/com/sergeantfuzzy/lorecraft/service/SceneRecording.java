package com.sergeantfuzzy.lorecraft.service;

import java.util.ArrayList;
import java.util.List;

class SceneRecording {
    final String id; final List<Runnable> actions = new ArrayList<>();
    SceneRecording(String id) { this.id = id; }
}