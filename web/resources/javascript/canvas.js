//$(document).ready(function () {
//    var canvas = document.getElementById("ex1");
//    var ctx = canvas.getContext("2d");
//    var image = new Image();
//    image.src = "resources/images/sample.jpg";
//    $(image).load(function () {
//        ctx.drawImage(image, 0, 0, 1000, 1000);
//        ctx.fillStyle = "white";
//	ctx.fillRect(0, 0, 1000, 1000);
////        ctx.fillRect(0, 202, 1000, 400);
////        ctx.fillRect(100, 402, 900, 398);
////        ctx.fillRect(0, 802, 1000, 400);
//    });
//    $(canvas).click(function (e) {
//        var canvasOffset = $(canvas).offset();
//        var canvasX = Math.floor(e.pageX - canvasOffset.left);
//        var canvasY = Math.floor(e.pageY - canvasOffset.top);
//        
//        var imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
//        var pixels = imageData.json1;
//        var pixelRedIndex = ((canvasY - 1) * (imageData.width * 4)) + ((canvasX - 1) * 4);
//        console.log(pixelRedIndex);
//        var pixelcolor = "rgba(" + pixels[pixelRedIndex] + ", " + pixels[pixelRedIndex + 1] + ", " + pixels[pixelRedIndex + 2] + ", " + pixels[pixelRedIndex + 3] + ")";
//        
//            ctx.drawImage(image,canvasX, canvasY, 100, 100, canvasX, canvasY, 100, 100);
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
//            
////        $("body").css("backgroundColor", pixelcolor);
//    });
//});







$(document).ready(function () {

    var imageObj = new Image();
    imageObj.src = 'resources/images/sample.jpg';
    imageObj.width = 1000;
    imageObj.height = 1000;
    imageObj.id = "image1";

    var canvas = document.getElementById('ex1');
    var ctx = canvas.getContext('2d');
    var imgW = imageObj.width;
    var imgH = imageObj.height;

    function reOffset() {
        var BB = canvas.getBoundingClientRect();
        offsetX = BB.left;
        offsetY = BB.top;
    }
    var offsetX, offsetY;
    reOffset();
    window.onscroll = function (e) {
        reOffset();
    }
    window.onresize = function (e) {
        reOffset();
    }
    $("#ex1").mousemove(function (e) {
        handleMouseChange(e);
    });
    $("#ex1").mouseout(function (e) {
        $("#nm").html("");
    });
    imageObj.onload = function () {
        canvas.width = imgW;
        canvas.height = imgH;
        ctx.fillStyle = ctx.createPattern(this, "no-repeat");
        makeRect();
    };
    function defineNames(mouseX, mouseY) {
        var CoOrX = 0;
        var CoOrY = 0;
        var iniCoordX = 0;
        var iniCoordY = 0;
        var q = JSON.parse($('#json1').html());
        ctx.beginPath();
        ctx.moveTo(0, 0);
        jQuery.each(q, function (i, val) {
            CoOrX = parseInt(q[i].pixelNumber);
            CoOrY = 1;
            ctx.rect(iniCoordX, iniCoordY, CoOrX, CoOrY);
            //ctx.rect(10, 10, 100, 100);
            ctx.fill();
            ctx.closePath();
            if (mouseX >= iniCoordX && mouseX <= (CoOrX + iniCoordX) && mouseY == (iniCoordY)) {
                $("#nm").html(q[i].revealName);
                return false;
            }
            iniCoordX += parseInt(CoOrX);
            if (iniCoordX >= 1000) {
                iniCoordX = 0;
                iniCoordY += 1;
            }
        });

    }

    function makeRect() {
        var q = JSON.parse($('#json1').html());
        var CoOrX = 0;
        var CoOrY = 0;
        var iniCoordX = 0;
        var iniCoordY = 0;
        ctx.beginPath();
        ctx.moveTo(0, 0);
        jQuery.each(q, function (i, val) {
            CoOrX = parseInt(q[i].pixelNumber);
            CoOrY = 1; //parseInt(offsetTop);
            ctx.rect(iniCoordX, iniCoordY, CoOrX, 1);
            ctx.fill();
            ctx.closePath();
            iniCoordX += CoOrX;
            if (iniCoordX >= 1000) {
                iniCoordX = 0;
                iniCoordY += 1;
            }
        });
    }
    function handleMouseChange(e) {
        // tell the browser we're handling this event
        e.preventDefault();
        e.stopPropagation();

        mouseX = parseInt(e.clientX - offsetX);
        mouseY = parseInt(e.clientY - offsetY);
        defineNames(mouseX, mouseY);
        if (ctx.isPointInPath(mouseX, mouseY)) {
            defineNames(mouseX, mouseY);
        } else {
            $("#nm").html("");
        }
    }

});
