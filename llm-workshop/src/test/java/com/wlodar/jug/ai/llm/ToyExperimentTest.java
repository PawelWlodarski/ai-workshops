package com.wlodar.jug.ai.llm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ToyExperimentTest {

    @Test
    void testGreeting() {
        ToyExperiment experiment = new ToyExperiment();
        assertEquals("Hello from ML-Toy!", experiment.getGreeting());
    }
}
