package com.phasec.plagsafe;
/**
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.phasec.plagsafe.detector.DetectionEngine;
import com.phasec.plagsafe.detector.Engine;
import com.phasec.plagsafe.objects.FileRecord;
import com.phasec.plagsafe.objects.Report;
import com.phasec.plagsafe.objects.SubmissionRecord;

import util.FileUtility;

/**
 * Service for starting comparison of files
 * @author sanketsaurav
 *
 */
@Service
public class ComparisonService {

    /**
     *
     * @param submissions
     * @param comparisonStrategy 
     */
    public List<Report> submissionStub(List<SubmissionRecord> submissions, StrategyType comparisonStrategy) {
        Engine detectionEngine = new DetectionEngine();
        return detectionEngine.runDetection(submissions, comparisonStrategy);
    }
    
    /**
     * 
     * @param filesList
     * @param comparisonStrategy 
     * @return
     */
    public List<Report> runComparisionForFiles(List<FileRecord> filesList, StrategyType comparisonStrategy){
		List<SubmissionRecord>  submissionRecords = new ArrayList<>();
		for(FileRecord files : filesList) {
			SubmissionRecord record = FileUtility.getFileMapList(files);
			submissionRecords.add(record);
		}
		List<Report> reports = submissionStub(submissionRecords, comparisonStrategy);
		Collections.sort(reports);
		return reports;
		
    }
}
