package com.phasec.plagsafe.detector;

import java.util.ArrayList;
import java.util.List;

import com.phasec.plagsafe.objects.Report;
import com.phasec.plagsafe.objects.SubmissibleRecord;

import util.LevenshteinDistanceGeneratorUtility;
import util.SubmissionUtility;

public class RenamingDetectionStrategy implements DetectionStrategy {
	
	private static final String MATCH_REMARK = "Renaming Similarity Measure ";


	/**
	 * the concrete method to show comparison result for two submissions
	 * @param submission1 a folder of submission with a list of submitted files
	 * @param submission2 another folder of submission with a list of submitted files
	 * @return a list of reports that contains all results of one-to-one comparison
	 */

	@Override
	public List<Report> compare(SubmissibleRecord submission1, SubmissibleRecord submission2) {
		List<Report> reportList = new ArrayList<>();
		for(Submissible sub1: submission1.getSubmissibles()){
			for(Submissible sub2: submission2.getSubmissibles()){
				int minimumEditDistance = LevenshteinDistanceGeneratorUtility.getLevenshteinDistance(sub1.getCode(), sub2.getCode());
				int averageFileLength = SubmissionUtility.getAverageSubmissionFileLength(sub1.getCode(), sub2.getCode());
				int matchPercentage = SubmissionUtility.getMatchPercentage(minimumEditDistance, averageFileLength);
				Report newReport = new Report(sub1.getName(), sub2.getName(), matchPercentage, MATCH_REMARK);
				reportList.add(newReport);				
			}
		}
		return reportList;
	}
}
