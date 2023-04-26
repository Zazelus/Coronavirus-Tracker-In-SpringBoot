# Important Notice

The repository containing the COVID-19 data has been archived as of 03/10/2023. The application in its current form only shows data relevant to 03/09/2023.

Please note that the data shown in the table below corresponds to the latest available data up to 03/09/2023.
# A SpringBoot Web Application That Tracks COVID-19 Cases
This web page fetches data from the CSSEGISandData (https://github.com/CSSEGISandData/COVID-19) repository in CSV format, parses it and displays it on a web page. The service is scheduled to update every hour to check if there are any changes.

# How Does it Work?
By using apache's commons csv library (https://commons.apache.org/proper/commons-csv/index.html), we can easily obtain each record in the csv files that we are fetching. We then add each record and relevant data to a model that is then used by a controller to display the output in html using the Spring Thymeleaf dependency.

# Running the Demo
  1. Download or clone the repository.
  2. Import into Intellij
  3. Go to File->Settings->Compiler->Java Compiler and set the version to Java 11.
  4. Build using Maven and run the project.
  5. Using your browser, go to localhost:8080 and it should display!
  
# Live Demo
If you'd like to see a live demo, I have it hosted on Heroku: https://coronavirus-tracker-poc.herokuapp.com/

# What's the Point of This?
Great question! There are a lot of COVID-19 dashboards online, with a much better user experience and more information. I've been using Java for a while and I wanted to learn SpringBoot, so I figured that a project like this would be great learning experience. Feel free to use this repo for your own needs!
