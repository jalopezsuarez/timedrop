package io.timedrop.business.report;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.timedrop.domain.Report;
import io.timedrop.service.ReportService;

public class ReportManager
{

	public void generateReport() throws Exception
	{
		ReportService reportService = new ReportService();
		ArrayList<Object> reports = reportService.fetchProjects();

		// -------------------------------------------------------

		StringBuilder html = new StringBuilder();
		html.append("<!doctype html>\n");
		html.append("<html lang='en'>\n");

		html.append("<head>\n");
		html.append("<meta charset='utf-8'>\n");
		html.append("<title>Report of Reports</title>\n");
		html.append("</head>\n\n");

		html.append("<body>\n");
		html.append("<h1>Projects / Tasks</h1>\n");

		html.append("<table>\n");
		for (Object report : reports)
		{
			if (report instanceof Report)
			{
				Report response = (Report) report;
				html.append("<tr>\n");

				html.append("<td>" + response.getProjectDescription() + "</td>\n");
				html.append("<td>" + response.getTaskDescription() + "</td>\n");

				long duration = response.getTaskDuration();
				long hours = TimeUnit.SECONDS.toHours(duration);
				duration -= TimeUnit.HOURS.toSeconds(hours);
				long minutes = TimeUnit.SECONDS.toMinutes(duration);
				duration -= TimeUnit.MINUTES.toSeconds(minutes);
				String timeString = String.format("%02d:%02d", hours, minutes);

				html.append("<td>" + timeString + "</td>\n");

				html.append("<tr>\n");
			}
		}
		html.append("</table>\n");

		html.append("</body>\n\n");
		html.append("</html>");

		// -------------------------------------------------------

		File file = new File("report.html");
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(html.toString());
		} finally
		{
			if (writer != null)
				writer.close();
		}

		// -------------------------------------------------------

		Desktop.getDesktop().browse(file.toURI());

	}
}
