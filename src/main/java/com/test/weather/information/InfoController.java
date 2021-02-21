package com.test.weather.information;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.jsf.el.WebApplicationContextFacesELResolver;

import com.test.weather.information.exceptions.USZipCodeNotFoundException;
import com.test.weather.information.model.Location;
import com.test.weather.information.model.TemperatureAtTime;
import com.test.weather.informations.service.TemperatureForecastService;
import com.test.weather.informations.service.TemperatureReportingService;
import com.test.weather.informations.service.ZipCodeDetailsService;

@RestController
public class InfoController {
	private static final Logger logger = LoggerFactory.getLogger(InfoController.class);
	@Autowired
	TemperatureForecastService temperatureForecastServiceimpl;
	@Autowired
	TemperatureReportingService temperatureReportingService;
	@Autowired
	ZipCodeDetailsService zipCodeDetailsService;


	@GetMapping("/weatherinfo")
	public TemperatureAtTime greeting(@RequestParam(value = "zipcode") String zipcode) throws USZipCodeNotFoundException {
		List<TemperatureAtTime> temperatures = null;
		TemperatureAtTime temperatureAtTime=null;
		logger.info("-----------the zip code is:"+zipcode);
		if(zipcode.equals("") || zipcode ==null  )
		{
			System.out.println("inside if condition of zip ");
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
			
		}
		LocalDate tomorrow = LocalDate.from(LocalDate.now()).plusDays(1);
		try {
			logger.info("this is logger");
			Location location = zipCodeDetailsService.getLocationFromZipCode(zipcode);
			System.out
					.println("Checking the weather for " + tomorrow.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
							+ " at " + zipcode + "(" + location.getName() + ")");
			temperatures = temperatureForecastServiceimpl.getTemperatureForeCastForGeoPosition(location);
			
			temperatureReportingService.printTemperatures(temperatures);
			temperatureAtTime=temperatureReportingService.getMinimumTemperature(temperatures);
			temperatureAtTime.setZipcode(zipcode);
			 
			logger.info("this is min temp;::::::::::::::::"+temperatureAtTime.getValue());
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}

		return temperatureAtTime;
	}
}
