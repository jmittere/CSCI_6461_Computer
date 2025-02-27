package com.example;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

public class SimulatorTest {
    Simulator sim = new Simulator();

    @Test
    public void testLoadFile(){
        
        String pf = "preload.txt";
        this.sim.setProgramFile(pf);
        boolean successInitialization = this.sim.initializeProgram();
        assertEquals(true, successInitialization);
    }

    @Test
    public void testStep(){
        String pf = "preload.txt";
        this.sim.setProgramFile(pf);
        this.sim.setPC(10);
        boolean successInitialization = this.sim.initializeProgram();
        HashMap<String, String> registerContents = this.sim.step(); //register contents returns a hashmap of all register contents after step()
        String debugOutput = registerContents.get("debugOutput");
        boolean stepError = !debugOutput.contains("ERROR");
        assertEquals(true, successInitialization);
        assertEquals(true, stepError);
    }
    @Test
    public void testRun(){
        String pf = "preload.txt";
        this.sim.setProgramFile(pf);
        this.sim.setPC(7);
        boolean successInitialization = this.sim.initializeProgram();
        HashMap<String, String> registerContents = this.sim.run(); //register contents returns a hashmap of all register contents after run()
        String debugOutput = registerContents.get("debugOutput");
        boolean runSuccess = !debugOutput.contains("ERROR");
        assertEquals(true, successInitialization);
        assertEquals(true, runSuccess);
    }
}
