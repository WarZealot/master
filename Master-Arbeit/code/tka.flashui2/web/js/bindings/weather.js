
$(document).ready(function() {
    createWeatherView();
    createWeatherLocationInput();
});


function createWeatherView(status) {
    var $ul = $("#bindings-list");

    if (status == "true") {
        $li = $("<li class='list-group-item list-group-item-success'><strong>Weather</strong></li>");
        $li.appendTo($ul);
        return;
    }

    $li = $("<li class='weather list-group-item list-group-item'><strong>Weather</strong></li>");
    $li.appendTo($ul);
}

function createWeatherLocationInput(url) {
    var $block = $("<div class='col-md-4 input-group weather-pin-group'><input class='form-control' placeholder='Location' type='text' id='weather-location'>   </div>");
    var $connectBtn = $(" <span class='input-group-btn'> <button class='btn btn-default' type='button'>Add</button>  </span>");
    $connectBtn.click(function(event) {
        var param = "location=" + $("#weather-location").val();
        $.ajax({
            dataType : "text",
            url : "http://localhost:8080/weather?" + param,
            success : function(data) {
                alert("Success: " + data);
                recreateWeatherView();
            },
        });
    });
    $connectBtn.appendTo($block);
    var $li = $("li.weather");
    $li.append($block);
}

function recreateWeatherView() {
    location.reload(true);
}