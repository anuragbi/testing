package com.test.weather.information.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.weather.information.exceptions.ResourceNotFoundException;
import com.test.weather.information.exceptions.SetupException;
import com.test.weather.information.integrations.response.model.ResponseCodeAndBody;
import com.test.weather.information.integrations.response.model.TemperatureData;
import com.test.weather.information.integrations.response.model.TemperatureResponse;
import com.test.weather.information.integrations.service.impl.HTTPClientService;
import com.test.weather.information.model.Location;
import com.test.weather.information.model.TemperatureAtTime;
import com.test.weather.informations.service.TemperatureForecastService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * This impplementation of Temperature forecast service uses an external API (Dark Sky)
 * to forecast the temperature at a given location (Latitude and Longitude)
 */
@Service
public class TemperatureForecastServiceImpl implements TemperatureForecastService {

    private final HTTPClientService httpClientService;
    @Value("${temperature.service.location}")
    private String temperatureServiceLocation;

    @Autowired
    public TemperatureForecastServiceImpl(HTTPClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    /**
     * The free version of api returns the forecast of multiple days. but since we need only tomorrow's forecast, all others must be filtered out.
     * It picks up system's date to decide tomorrow's date, hence if system's date is not correct the results might be buggy.
     *
     * @param location : the latitude and longitude of the location for which temperature forecast is required
     * @return : the timings and temperature for tomorrow.
     */
    @Override
    public List<TemperatureAtTime> getTemperatureForeCastForGeoPosition(Location location) {
        List<TemperatureAtTime> temperatures = new ArrayList<>();

        try {
            ResponseCodeAndBody responseCodeAndBody = httpClientService.get(temperatureServiceLocation.replace("{lat}", String.valueOf(location.getLatitude())).replace("{long}", String.valueOf(location.getLongitude())));

            LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
            Gson gson = new Gson();
            Type type = new TypeToken<TemperatureResponse>() {
            }.getType();
            TemperatureResponse temperatureResponse = gson.fromJson(responseCodeAndBody.getBody(), type);

            for (TemperatureData data : temperatureResponse.getHourly().getData()) {
                LocalDateTime date = Instant.ofEpochMilli(data.getTime() * 1000).atZone(ZoneId.systemDefault()).toLocalDateTime();
                if (date.getDayOfYear() == tomorrow.getDayOfYear()) {
                    temperatures.add(new TemperatureAtTime(data.getTemperature(), date,0, temperatureServiceLocation));
                }
            }
            
            if (temperatures.size() == 0) {
                throw new ResourceNotFoundException("The service was unable to fetch relevant temperatures");
            }


        } catch (IOException e) {
            throw new SetupException("IO exception during invocation of REST call to " + temperatureServiceLocation);
        }
        return temperatures;
    }
}
