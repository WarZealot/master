/**
 * 
 */

$(document).ready(function() {
    $.getJSON("http://localhost:8080/flashthingsprovider", function(data) {
        repopulateThingsTable(data);
    });
});

function repopulateThingsTable(data) {
    var $tbody = $("#things-view tbody");
    $tbody.empty();

    $.each(data, function(index, thing) {
        var $row = $("<tr/>");
        $row.append("<td>" + thing.uid.segments.join(":") + "</td>");
        $row.append("<td>" + thing.label + "</td>");
        
        var channelsArray = new Array();
        $.each(thing.channels, function(index, channel) {
            channelsArray.push(channel.channelTypeUID.segments.pop());
        });
        
        $row.append("<td>" + channelsArray.join(",") + "</td>");

        var $cell = $("<td/>");
        $row.append($cell);
        
        var $deleteBtn = $("<button class='btn btn-danger' data-uid='" + thing.uid.segments.join(":") + "' > Delete </button>");
        $deleteBtn.click(function(event) {
            $btn = $(event.target);
            var uid = $btn.data("uid");
            var param = "delete=" + uid;
            $.ajax({
                dataType : "json",
                url : "http://localhost:8080/flashthingsdeleter?" + param,
                success : function(data) {
                    if (data == "SUCCESS") {
//                        $btn.parents("tr").remove();
                        location.reload(true);
                    } else {
                        alert("Could not delete thing... Sorry!");
                    }
                },
                error : function(msg) {
                    console.log("error during ajax request")
                    console.log(msg);
                },
            });
        });

        var $cell2 = $("<td/>");
        $cell2.append($deleteBtn);
        $row.append($cell2);
        
        $row.appendTo($tbody);
    });
}
