package com.phasec.plagsafe.objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;

public class SystemStatistics implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(SystemStatistics.class);
    //serialized file location
    private static final String FILE_PATH = "src/main/resources/stats.ser";

    // start date of the system
    private Date systemStartDate;

    // previous use of the system
    private Date systemLastUsed;

    public void updateSystemLastUsed() {
        systemLastUsed = new Date();
    }

    // total number of times the system has run since the start date
    private int totalRuns;

    public void incrementTotalRunsBy(int i) {
        totalRuns += i;
    }
    
    // count of the total requests made by the user for each type of strategy
    private int logicalComparisonRequested;
    private int renamingComparisonRequested;
    private int refactoringComparisonRequested;
    private int weightedComparisonRequested;

    public void incrementLogicalComparisonRequestedBy(int i) {
        logicalComparisonRequested += i;
    }

    public void incrementRenamingComparisonRequestedRunsBy(int i) {
        renamingComparisonRequested += i;
    }

    public void incrementRefactoringComparisonRequested(int i) {
        refactoringComparisonRequested += i;
    }

    public void incrementWeightedComparisonRequestedRunsBy(int i) {
        weightedComparisonRequested += i;
    }

    // count of total number of files compared
    private int totalFilesCompared;

    public void incrementTotalFilesComparedBy(int i) {
        totalFilesCompared += i;
    }

    // maximum number of files compared in a single run
    private int maxSystemLoad;

    public void updateMaxLoad(int currentLoad) {
        maxSystemLoad = Math.max(maxSystemLoad, currentLoad);
    }

    // number of times the system has crashed
    private int systemFailures;

    public void incrementSystemFailuresBy(int i) {
        systemFailures += i;
    }

    private static SystemStatistics statsInstance;

    /**
     * de-serialize data usage data in this method
     */
    private SystemStatistics() {
    }


    /**
     * method to make sure only one instance of the object gets passed
     * @return instance of this class
     */

    public static SystemStatistics initializeSystemStatistics() {
        if(statsInstance == null)
            statsInstance = new SystemStatistics();
        return statsInstance;
    }

    /**
     * to be used if the system stats need to be reset
     */
    public void resetSystemStats() {
        resetStats();
        serializeStats();
    }

    /**
     * resets all data members to counts to 0, and dates to today's date for the data members
     */
    private void resetStats() {
        systemStartDate = new Date();
        systemLastUsed = new Date();
        totalRuns = 0;
        logicalComparisonRequested = 0;
        renamingComparisonRequested = 0;
        refactoringComparisonRequested = 0;
        weightedComparisonRequested = 0;
        totalFilesCompared = 0;
        maxSystemLoad = 0;
        systemFailures = 0;
    }

    /**
     * Serializes this object
     */
    public void serializeStats() {
        try {
            FileOutputStream outputFile = new FileOutputStream(FILE_PATH);
            ObjectOutputStream out = new ObjectOutputStream(outputFile);
            out.writeObject(statsInstance);

            out.close();
            outputFile.flush();
        } catch(IOException e) {
            logger.error("Object serialization exception " + e);
        }
    }


    public void loadSystemStats() {
        try {
            FileInputStream inputFile = new FileInputStream(FILE_PATH);
            ObjectInputStream input = new ObjectInputStream(inputFile);
            statsInstance = (SystemStatistics) input.readObject();

        } catch(IOException | ClassNotFoundException e) {
            logger.error("Object deserialization exception " + e);
        }
    }


    @Override
    public String toString() {
        return "System started on:\t\t" + systemStartDate + "\n" +
                "System last run on:\t\t" + systemLastUsed + "\n" +
                "Total system runs\t\t" + totalRuns + "\n" +
                "Logical comparison strategy requests:\t\t" + logicalComparisonRequested + "\n" +
                "Renaming comparison strategy requests:\t\t" + renamingComparisonRequested + "\n" +
                "Refactoring comparison strategy requests:\t\t" + refactoringComparisonRequested + "\n" +
                "Weighted comparison strategy requests:\t\t" + weightedComparisonRequested + "\n" +
                "Total number of files compared:\t\t" + totalFilesCompared + "\n" +
                "Maximum load in seen in a run:\t\t" + maxSystemLoad + "\n" +
                "System Failures:\t\t" + systemFailures + "\n";
    }


    public Date getSystemStartDate() {
        return systemStartDate;
    }

    public void setSystemStartDate(Date systemStartDate) {
        this.systemStartDate = systemStartDate;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;
    }

    public int getLogicalComparisonRequested() {
        return logicalComparisonRequested;
    }

    public void setLogicalComparisonRequested(int logicalComparisonRequested) {
        this.logicalComparisonRequested = logicalComparisonRequested;
    }

    public int getRenamingComparisonRequested() {
        return renamingComparisonRequested;
    }

    public void setRenamingComparisonRequested(int renamingComparisonRequested) {
        this.renamingComparisonRequested = renamingComparisonRequested;
    }

    public int getRefactoringComparisonRequested() {
        return refactoringComparisonRequested;
    }

    public void setRefactoringComparisonRequested(int refactoringComparisonRequested) {
        this.refactoringComparisonRequested = refactoringComparisonRequested;
    }

    public int getWeightedComparisonRequested() {
        return weightedComparisonRequested;
    }

    public void setWeightedComparisonRequested(int weightedComparisonRequested) {
        this.weightedComparisonRequested = weightedComparisonRequested;
    }

    public int getTotalFilesCompared() {
        return totalFilesCompared;
    }

    public void setTotalFilesCompared(int totalFilesCompared) {
        this.totalFilesCompared = totalFilesCompared;
    }

    public int getMaxSystemLoad() {
        return maxSystemLoad;
    }

    public void setMaxSystemLoad(int maxSystemLoad) {
        this.maxSystemLoad = maxSystemLoad;
    }

    public int getSystemFailures() {
        return systemFailures;
    }

    public void setSystemFailures(int systemFailures) {
        this.systemFailures = systemFailures;
    }

    public Date getSystemLastUsed() {
        return systemLastUsed;
    }

    public void setSystemLastUsed(Date systemLastUsed) {
        this.systemLastUsed = systemLastUsed;
    }
}