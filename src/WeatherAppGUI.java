import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGUI extends JFrame {
    private JSONObject weatherData;
    private JTextField searchTextField;
    private JLabel weatherConditionImage;
    private JLabel temperatureText;
    private JLabel weatherConditionDesc;
    private JLabel humidityImage;
    private JLabel humidityText;
    private JLabel windspeedImage;
    private JLabel windspeedText;
    private JButton searchButton;
    private String currentCity;
    private JLabel cityText;
    public WeatherAppGUI() {
        // title
        super("Weather App");
        currentCity = WeatherApp.getCurrentLocation();
        // ends progam when closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // in pixels
        setSize(450, 650);

        // load our GUI at the center of the screen
        setLocationRelativeTo(null);

        // layout manager null to manually position our components
        setLayout(null);

        //prevent resizing
        setResizable(false);

        addGuiComponents();

    }
    private void addGuiComponents() {
        // text field
        searchTextField = new JTextField();

        // set location and size
        searchTextField.setBounds(15, 15, 351, 45);

        // change font
        searchTextField.setFont(new Font("dialog", Font.PLAIN, 24));

        searchTextField.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               search();
           }
        });

        add(searchTextField);

        //city text
        cityText = new JLabel(currentCity);
        cityText.setFont(new Font("dialog", Font.PLAIN, 24));
        cityText.setBounds(0, 130, 351, 45);
        add(cityText);

        //weather image
        weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);

        //temperature text
        temperatureText = new JLabel("10 C");
        temperatureText.setFont(new Font("dialog", Font.BOLD, 48));
        temperatureText.setBounds(0, 350, 450, 54);

        //center the text
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        //weather condition description
        weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setFont(new Font("dialog", Font.PLAIN, 32));
        weatherConditionDesc.setBounds(0, 405, 450, 36);
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        //humidity image
        humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        add(humidityImage);

        //humidity text
        humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setFont(new Font("dialog", Font.PLAIN, 16));
        humidityText.setBounds(90, 500, 85, 55);
        add(humidityText);

        //windspeed image
        windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220, 500, 74, 66);
        add(windspeedImage);

        //windspeed text
        windspeedText = new JLabel("<html><b>Windspeed</b> 15km</html>");
        windspeedText.setFont(new Font("dialog", Font.PLAIN, 16));
        windspeedText.setBounds(310, 500, 85, 55);
        add(windspeedText);
        weatherData = WeatherApp.getWeatherData(currentCity);
        updateGUI(weatherData);

        //create search button
        searchButton = new JButton(loadImage("src/assets/search.png"));

        //change cursor to a hand while hovering
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //set location
        searchButton.setBounds(375, 13, 47, 45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               search();


            }
        });
        add(searchButton);
    }
    private ImageIcon loadImage(String resourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        }catch(IOException e) {
            e.printStackTrace();
        }
        System.out.println("Could not find resource");
        return null;
    }

    private void updateGUI(JSONObject weatherData){


        cityText.setText(searchTextField.getText());
        searchTextField.setText("");
        //update weather image
        String weatherCondition = (String) weatherData.get("weather_condition");

        switch (weatherCondition) {
            case "Clear":
                weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                break;
            case "Cloudy":
                weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                break;
            case "Rainy":
                weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                break;
            case "Snowy":
                weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                break;
        }

        //update temperature text
        double temperature = (Double) weatherData.get("temperature");
        temperatureText.setText(temperature + "C");

        //update weather condition text
        weatherConditionDesc.setText(weatherCondition);

        //update humidity text
        long humidity = (Long) weatherData.get("humidity");
        humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

        //update wind speed text
        double windspeed = (double) weatherData.get("windspeed");
        windspeedText.setText("<html><b>WindSpeed</b> " + windspeed + "km/h</html>");
    }
    private void search(){
        //get location from user
        String userInput = searchTextField.getText();

        //validate input - remove whitespace to ensure non-empty text
        if (userInput.replaceAll("\\s", "").length() <= 0) {
            return;
        }

        //retrieve weather data
        weatherData = WeatherApp.getWeatherData(userInput);

        //UPDATE GUI

        updateGUI(weatherData);
    }
}
