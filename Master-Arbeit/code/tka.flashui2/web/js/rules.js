/**
 * 
 */

$(document).ready(function() {
    $.getJSON("http://localhost:8080/flashrulesprovider", function(data) {
        repopulateTable(data);
    });

    bindHandlers();
});

function bindHandlers() {
    $("#importBtn").click(function() {
        var $text = $("#rule-json");
        $.ajax({
            dataType : "text",
            url : "http://localhost:8080/flashrulesimporter",
            data : $text.val(),
            success : function(data) {
                if (data == "SUCCESS") {
                    location.reload(true);
                } else {
                    alert("Importing the rule failed.");
                }
            }
        });
    });
}

function repopulateTable(data) {
    var $tbody = $("#rules-view tbody");
    $tbody.empty();

    $.each(data, function(index, rule) {
        var $row = $("<tr/>");
        $row.append("<td>" + rule.uid + "</td>");
        $row.append("<td>" + rule.description + "</td>");
        $row.append("<td>" + rule.enabled + "</td>");

        var $showBtn = $("<button class='btn btn-default' data-uid='" + rule.uid + "' > Show </button>");
        $showBtn.click(function(event) {
            $btn = $(event.target);
            var uid = $btn.data("uid");
            var param = "uid=" + uid;
            $.ajax({
                dataType : "json",
                url : "http://localhost:8080/flashrulesprovider?" + param,
                success : function(data) {
                    var $text = $("#rule-json");
                    $text.val(JSON.stringify(data, null, 4));
                },
                error : function(msg) {
                    console.log("error during ajax request")
                    console.log(msg);
                },
            });
        });
        
        var $toggleBtn = $("<button class='btn btn-warning' data-uid='" + rule.uid + "' > Toggle </button>");
        $toggleBtn.click(function(event) {
            $btn = $(event.target);
            var uid = $btn.data("uid");
            var param = "toggle=" + uid;
            $.ajax({
                dataType : "json",
                url : "http://localhost:8080/flashrulesdeleter?" + param,
                success : function(data) {
                    if (data == "SUCCESS") {
                        location.reload(true);
                    } else {
                        alert("Could not toggle rule... Sorry!");
                    }
                },
                error : function(msg) {
                    console.log("error during ajax request")
                    console.log(msg);
                },
            });
        });

        var $deleteBtn = $("<button class='btn btn-danger' data-uid='" + rule.uid + "' > Delete </button>");
        $deleteBtn.click(function(event) {
            $btn = $(event.target);
            var uid = $btn.data("uid");
            var param = "delete=" + uid;
            $.ajax({
                dataType : "json",
                url : "http://localhost:8080/flashrulesdeleter?" + param,
                success : function(data) {
                    if (data == "SUCCESS") {
                        $btn.parents("tr").remove();
                    } else {
                        alert("Could not delete rule... Sorry!");
                    }
                },
                error : function(msg) {
                    console.log("error during ajax request")
                    console.log(msg);
                },
            });
        });

        var $cell = $("<td/>");
        $cell.append($showBtn);
        $cell.append($toggleBtn);
        $cell.append($deleteBtn);
        $row.append($cell);
        $row.appendTo($tbody);
    });
}
