
(function (a) {
    a.createModal = function (b)
    {
        defaults = {title: "", message: "Your Message Goes Here!", closeButton: true, scrollable: false};
        var b = a.extend({}, defaults, b);
        var c = (b.scrollable === true) ? 'style="max-height: 420px;overflow-y: auto;"' : "";
        html = '<div class="modal fade" id="myModal">';
        html += '<div class="modal-dialog">';
        html += '<div class="modal-content">';
        html += '<div class="modal-header">';
        html += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>';
        if (b.title.length > 0) {
            html += '<h4 class="modal-title">' + b.title + "</h4>"
        }
        html += "</div>";
        html += '<div class="modal-body" ' + c + ">";
        html += b.message;
        html += "</div>";
        html += '<div class="modal-footer">';
        if (b.closeButton === true) {
            html += '<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>'
        }
        html += "</div>";
        html += "</div>";
        html += "</div>";
        html += "</div>";
        a("body").prepend(html);
        a("#myModal").modal().on("hidden.bs.modal", function () {
            a(this).remove()
        })
    }
})(jQuery);
$(function () {
    $('.view-pdf').on('click', function () {
        var pdf_link = $(this).attr('href');
        //var iframe = '<div class="iframe-container"><iframe src="'+pdf_link+'"></iframe></div>'
        //var iframe = '<object data="'+pdf_link+'" type="application/pdf"><embed src="'+pdf_link+'" type="application/pdf" /></object>'        
        var iframe = '<object type="application/pdf" data="' + pdf_link + '" width="100%" height="500">No Support</object>'
        $.createModal({
            title: '',
            message: iframe,
            closeButton: true,
            scrollable: false
        });
        return false;
    });
})

// ===========================================

$("#co").click(function () {
    $(".navbar-default,.footer,.btn-default1").css("background-color", "#337ab7");
    $(".togglediv").css("background-color", "#5d9ed6");
    $("wrapper,.header").css("background-color", "#e5f3fd");
    $(".dropdown-menu,.digipharma").css("background-color", "#337ab7");
    $("#wowslider-container1 a.ws_next > span, #wowslider-container1 a.ws_prev > span, #wowslider-container1 .ws_bullets a > span, #wowslider-container1 .ws_playpause").css("background-color", "#337ab7");
});
$("#co1").click(function () {
    $(".navbar-default,.footer,.btn-default1").css("background-color", "#16bec6");
    $(".togglediv").css("background-color", "#3cdbe2");
    $("wrapper,.header").css("background-color", "#d8f9fb");
    $(".dropdown-menu,.digipharma").css("background-color", "#16bec6");
    $("#wowslider-container1 a.ws_next > span, #wowslider-container1 a.ws_prev > span, #wowslider-container1 .ws_bullets a > span, #wowslider-container1 .ws_playpause").css("background-color", "#16bec6");
});
$("#co2").click(function () {
    $(".navbar-default,.footer,.btn-default1").css("background-color", "#d9534f");
    $(".togglediv").css("background-color", "#e47572");
    $("wrapper,.header").css("background-color", "#f7dddc");
    $(".dropdown-menu,.digipharma").css("background-color", "#d9534f");
    $("#wowslider-container1 a.ws_next > span, #wowslider-container1 a.ws_prev > span, #wowslider-container1 .ws_bullets a > span, #wowslider-container1 .ws_playpause").css("background-color", "#d9534f");
});
$("#co3").click(function () {
    $(".navbar-default,.footer,.btn-default1").css("background-color", "#a55e97");
    $(".togglediv").css("background-color", "#cc88be");
    $("wrapper,.header").css("background-color", "#f3ddee");
    $(".dropdown-menu,.digipharma").css("background-color", "#a55e97");
    $("#wowslider-container1 a.ws_next > span, #wowslider-container1 a.ws_prev > span, #wowslider-container1 .ws_bullets a > span, #wowslider-container1 .ws_playpause").css("background-color", "#a55e97");
});
$("#co4").click(function () {
    $(".navbar-default,.footer,.btn-default1").css("background-color", "#8a6d3b");
    $(".togglediv").css("background-color", "#a58858");
    $("wrapper,.header").css("background-color", "#f7e9d2");
    $(".dropdown-menu,.digipharma").css("background-color", "#8a6d3b");
    $("#wowslider-container1 a.ws_next > span, #wowslider-container1 a.ws_prev > span, #wowslider-container1 .ws_bullets a > span, #wowslider-container1 .ws_playpause").css("background-color", "#8a6d3b");
});

//=====================================
$(document).ready(function () {
    $(".togglebtn").click(function () {
        $(".togglediv").toggle(500);
    });
});

var acc = document.getElementsByClassName("accordion");
var i;
for (i = 0; i < acc.length; i++) {
    acc[i].addEventListener("click", function () {
        this.classList.toggle("active");
    });
}


//=========================font
$(document).ready(function () {
    $('#linkIncrease').click(function () {
        modifyFontSize('increase');
    });

    $('#linkDecrease').click(function () {
        modifyFontSize('decrease');
    });

    $('#linkReset').click(function () {
        modifyFontSize('reset');
    })

    function modifyFontSize(flag) {
        var divElement = $('#divContent');
        var currentFontSize = parseInt(divElement.css('font-size'));

        if (flag == 'increase') {
            if (currentFontSize <= 19) {
                currentFontSize += 1;
            }
        } else if (flag == 'decrease') {
            if (currentFontSize >= 10) {
                currentFontSize -= 1;
            }
        } else
            currentFontSize = 15;

        divElement.css('font-size', currentFontSize);
    }
});


//==================
/*
 $(document).ready(function () {
 //Disable cut copy paste
 $('body').bind('cut copy paste', function (e) {
 e.preventDefault();
 });
 
 //Disable mouse right click
 $("body").on("contextmenu",function(e){
 return false;
 });
 });
 */

//=============================
$(document).ready(function () {
    $('#linkIncrease').click(function () {
        modifyFontSize('increase');
    });

    $('#linkDecrease').click(function () {
        modifyFontSize('decrease');
    });

    $('#linkReset').click(function () {
        modifyFontSize('reset');
    })

    function modifyFontSize(flag) {
        var divElement = $('#divContent');
        var currentFontSize = parseInt(divElement.css('font-size'));

        if (flag == 'increase')
            currentFontSize += 3;
        else if (flag == 'decrease')
            currentFontSize -= 3;
        else
            currentFontSize = 16;

        divElement.css('font-size', currentFontSize);
    }
});

//==========count=================

        $(document).ready(function () {
            $.ajax({
                method: "GET",
                url: "http://164.100.187.132/countForStaticPage.do",
                success: function (data) {
                    $("#institute_reg").text(data["institute_reg"]);
                    $("#inspector_reg").text(data["inspector_reg"]);
                    $("#pharmacist_reg").text(data["pharmacist_reg"]);
                    $("#faculty_reg").text(data["faculty_reg"]);
                }
            });
        });

//-------skip main content-------

function goBack() {
  window.history.back();
}