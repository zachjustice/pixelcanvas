"use strict";
var stompClient = null;

// get some info about the canvas
var canvas = null;
var ctx = null;

// how many cells fit on the canvas
var size = null;
var w = null;
var h = null;

var curr_color = null;

$(init);

function init() {
    size = 20;

    // get some info about the canvas
    canvas = document.getElementById('canvas');
    ctx = canvas.getContext('2d');

    // how many cells fit on the canvas
    w = ~~ (canvas.width / size);
    h = ~~ (canvas.height / size);
    curr_color = 'black';


    $('.palette-color').click(change_color_handler);
    $(canvas).click(click_canvas_handler);

    // connect after initializing so things are setup
    get_grid();
    connect();

    //$( "#disconnect" ).click(function() { disconnect(); });
}

function get_grid() {
    $.get('/pixels').done(function(pixels)
    {
        $.each(pixels, function(index, pixel) {
            console.log( index + ": (" + pixel.x + "," + pixel.y + ") " + pixel.color );
            fill(pixel.x, pixel.y, pixel.color);
        });
    });
}

function click_canvas_handler(e) {
    // get mouse click position
    var mx = e.offsetX;
    var my = e.offsetY;

    // calculate grid square numbers
    var x = ~~ (mx / size);
    var y = ~~ (my / size);

    // make sure we're in bounds
    if (x < 0 || x >= w || y < 0 || y >= h) {
        return;
    }

    fill(x, y, curr_color);
    sendPixel(x, y, curr_color);
}

function change_color_handler(e) {
    curr_color = $(this).css("background-color");
}

function connect() {
    var socket = new SockJS('/canvas');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/canvas', function (response) {
            console.log("subscribe to canvas");
            var pixel = JSON.parse(response.body);
            fill(pixel.x, pixel.y, pixel.color);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendPixel(x, y, color) {
    var pixel = JSON.stringify({'x': x, 'y': y, 'color': color});

    $.ajax({
        url: "/pixels",
        type: "POST",
        data: pixel,
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function (msg) {
        console.log(msg);
    })
}

function fill(x, y, color) {
    ctx.fillStyle = color;
    ctx.fillRect(x * size, y * size, size, size);
}



