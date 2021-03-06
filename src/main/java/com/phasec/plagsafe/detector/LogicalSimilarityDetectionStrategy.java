package com.phasec.plagsafe.detector;

import java.util.ArrayList;
import java.util.List;

import com.phasec.plagsafe.services.SystemStatisticsService;
import org.springframework.stereotype.Service;

import com.phasec.plagsafe.antlr.ASTPrinter;
import com.phasec.plagsafe.models.Report;
import com.phasec.plagsafe.models.SubmissibleRecord;

import util.SubmissionUtility;


@Service
public class LogicalSimilarityDetectionStrategy implements DetectionStrategy {

	private static final String MATCHING_REMARK = "Logical similarities detected.";



	/**
	 * the concrete method to show comparison result for two submissions
	 * 
	 * @param submission1 a folder of submission with a list of submitted files
	 * @param submission2 another folder of submission with a list of submitted
	 *            files
	 * @return a list of reports that contains all results of one-to-one comparison
	 */

	@Override
	public List<Report> compare(SubmissibleRecord submission1, SubmissibleRecord submission2) {
		List<Report> reportList = new ArrayList<>();

		for (Submissible sub1file : submission1.getSubmissibles()) {
			for (Submissible sub2file : submission2.getSubmissibles()) {
				Report current = fileASTCompare(sub1file, sub2file);
				reportList.add(current);
			}
		}

		return reportList;
	}



	/**
	 * takes to submission files and returns the match percentage
	 *
	 * @param sub1file
	 * @param sub2file
	 * @return logical matching percentage
	 *
	 */
	@Override
	public int compare(Submissible sub1file, Submissible sub2file) {
		Report report = fileASTCompare(sub1file, sub2file);
		return report.getMatchPercentage();
	}

    /**
     * updates the logical comparison request count
     *
     */

	@Override
	public void updateRequestCount() {
		SystemStatisticsService.incrementLogicalComparisonRequestedBy(1);
	}


	/**
	 * compare two submitted files
	 * 
	 * @param sub1file submission1 a folder of submission with a list of submitted
	 *            files
	 * @param sub2file another folder of submission with a list of submitted files
	 * @return the report of comparison of two single files
	 */

	private Report fileASTCompare(Submissible sub1file, Submissible sub2file) {
		ASTPrinter astIterator = new ASTPrinter();

		StringBuilder sb = new StringBuilder();
		astIterator.astString(sub1file.getAst(), sb);
		String ast1String = sb.toString();

		sb = new StringBuilder();
		astIterator.astString(sub2file.getAst(), sb);
		String ast2String = sb.toString();
		int renameCount = SubmissionUtility.editDistance(ast1String, ast2String);
		int averageFileLength = SubmissionUtility.getTotalSubmissionFileLength(ast1String, ast2String);
		int matchPercentage = SubmissionUtility.getMatchPercentage(renameCount, averageFileLength);

		return new Report(sub1file.getName(), sub2file.getName(), matchPercentage, MATCHING_REMARK);
	}
}
