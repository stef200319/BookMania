package nl.tudelft.sem.template.example.analyticsHandlers;

import nl.tudelft.sem.template.example.exceptions.InvalidAnalyticsException;
import nl.tudelft.sem.template.example.exceptions.InvalidDataException;
import nl.tudelft.sem.template.example.model.Analytics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AnalyticsLoginDateValidator extends BaseAnalyticsValidator{
    public  AnalyticsLoginDateValidator() {}

    @Override
    public boolean handle(Analytics analytics) throws InvalidAnalyticsException {
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            if (sdformat.parse(analytics.getLastLoginDate()).compareTo(sdformat.parse(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()))) > 0) throw new InvalidDataException("The date of the last login is illegal.");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return super.handle(analytics);
    }
}
