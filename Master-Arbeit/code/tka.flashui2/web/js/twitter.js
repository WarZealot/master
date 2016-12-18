/**
 * 
 */

$(document).ready(function() {
    requestTwitterConnectionStatus();
});

function requestTwitterConnectionStatus() {
    $.ajax({
        dataType : "text",
        url : "http://localhost:8080/twitter?status=a",
        success : function(data) {
            createTwitterView(data);
        }
    });
}

function createTwitterView(status) {
    var $ul = $("#bindings-list");

    if (status == "true") {
        $li = $("<li class='list-group-item list-group-item-success'><strong>Twitter</strong></li>");
        $li.appendTo($ul);
        return;
    }

    $li = $("<li class='twitter list-group-item list-group-item'><strong>Twitter</strong></li>");
    var $connectBtn = $("<button class='btn btn-success'> Connect </button>");
    $connectBtn.click(function(event) {
        $btn = $(event.target);
        $.ajax({
            dataType : "text",
            url : "http://localhost:8080/twitter",
            success : function(data) {
                createTwitterPinInputAndRedirect(data);
            },
        });
    });
    $connectBtn.appendTo($li);
    $li.appendTo($ul);

}

function createTwitterPinInputAndRedirect(url) {
    var $block = $("<div class='col-md-4 input-group twitter-pin-group'><input class='form-control' placeholder='Please enter PIN here' type='text' id='twitter-pin'>   </div>");
    var $connectBtn = $(" <span class='input-group-btn'> <button class='btn btn-default' type='button'>Go!</button>  </span>");
    $connectBtn.click(function(event) {
        var param = "pin=" + $("#twitter-pin").val();
        $.ajax({
            dataType : "text",
            url : "http://localhost:8080/twitter?" + param,
            success : function(data) {
                alert("Success: " + data);
                recreateTwitterView();
            },
        });
    });
    $connectBtn.appendTo($block);
    var $li = $("li.twitter");
    $li.append($block);

    var win = window.open(url, '_blank');
    if (win) {
        // Browser has allowed it to be opened
        win.focus();
    } else {
        // Browser has blocked it
        alert('Please allow popups for this website');
    }
}

function recreateTwitterView() {
//    var $li = $("li.twitter");
//    $li.remove();
//    requestTwitterConnectionStatus();
    location.reload(true);
}