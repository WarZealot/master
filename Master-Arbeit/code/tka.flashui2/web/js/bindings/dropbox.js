/**
 * 
 */

$(document).ready(function() {
    requestDropboxConnectionStatus();
});

function requestDropboxConnectionStatus() {
    $.ajax({
        dataType : "text",
        url : "http://localhost:8080/dropbox?status=a",
        success : function(data) {
            createDropboxView(data);
        }
    });
}

function createDropboxView(status) {
    var $ul = $("#bindings-list");

    if (status == "true") {
        $li = $("<li class='list-group-item list-group-item-success'><strong>Dropbox</strong></li>");
        $li.appendTo($ul);
        return;
    }

    $li = $("<li class='dropbox list-group-item list-group-item'><strong>Dropbox</strong></li>");
    var $connectBtn = $("<button class='btn btn-success'> Connect </button>");
    $connectBtn.click(function(event) {
        $btn = $(event.target);
        $.ajax({
            dataType : "text",
            url : "http://localhost:8080/dropbox",
            success : function(data) {
                createDropboxPinInputAndRedirect(data);
            },
        });
    });
    $connectBtn.appendTo($li);
    $li.appendTo($ul);

}

function createDropboxPinInputAndRedirect(url) {
    var $block = $("<div class='col-md-4 input-group dropbox-pin-group'><input class='form-control' placeholder='Please enter PIN here' type='text' id='dropbox-pin'>   </div>");
    var $connectBtn = $(" <span class='input-group-btn'> <button class='btn btn-default' type='button'>Go!</button>  </span>");
    $connectBtn.click(function(event) {
        var param = "pin=" + $("#dropbox-pin").val();
        $.ajax({
            dataType : "text",
            url : "http://localhost:8080/dropbox?" + param,
            success : function(data) {
                alert("Success: " + data);
                recreateDropboxView();
            },
        });
    });
    $connectBtn.appendTo($block);
    var $li = $("li.dropbox");
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

function recreateDropboxView() {
    location.reload(true);
}