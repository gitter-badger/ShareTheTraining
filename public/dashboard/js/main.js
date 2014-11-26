var dates = new Array();
var keyPoints = new Array();
var courses = new Array();
var keyPointNum = 0;
var JsonData;
var keyPointsString='';
var initKeyPointsString='';

var api;

function getJsonData() {
    JsonData = getFormJson($('form'));
    JsonData['courseDates'] = dates;
    keyPointsString='';
    var len=keyPoints.length;
    for(var i=0;i<len;i++)
    {
        if(0==i)
            keyPointsString+=keyPoints[i];
        else
            keyPointsString+=(','+keyPoints[i]);
    }
//    if(''!=initKeyPointsString){
//    	if(''!=keyPointsString)
//    		keyPointsString=initKeyPointsString+','+keyPointsString;
//    	else
//    		keyPointsString=initKeyPointsString;
//    }
//    else
//    	keyPointsString=initKeyPointsString;
    	JsonData['keyPoints'] = keyPointsString;
}

function getFormJson(frm) {
    var o = {};
    var a = $(frm).serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });

    return o;
}

$(document).ready(function () {
    //themes, change CSS with JS
    //default theme(CSS) is cerulean, change it if needed
    var defaultTheme = 'cerulean';

    var currentTheme = $.cookie('currentTheme') == null ? defaultTheme : $.cookie('currentTheme');
    var msie = navigator.userAgent.match(/msie/i);
    $.browser = {};
    $.browser.msie = {};
    switchTheme(currentTheme);

    $('.navbar-toggle').click(function (e) {
        e.preventDefault();
        $('.nav-sm').html($('.navbar-collapse').html());
        $('.sidebar-nav').toggleClass('active');
        $(this).toggleClass('active');
    });

    var $sidebarNav = $('.sidebar-nav');

    // Hide responsive navbar on clicking outside
    $(document).mouseup(function (e) {
        if (!$sidebarNav.is(e.target) // if the target of the click isn't the container...
            && $sidebarNav.has(e.target).length === 0 && !$('.navbar-toggle').is(e.target) && $('.navbar-toggle').has(e.target).length === 0 && $sidebarNav.hasClass('active')
        ) // ... nor a descendant of the container
        {
            e.stopPropagation();
            $('.navbar-toggle').click();
        }
    });


    $('#themes a').click(function (e) {
        e.preventDefault();
        currentTheme = $(this).attr('data-value');
        $.cookie('currentTheme', currentTheme, {
            expires: 365
        });
        switchTheme(currentTheme);
    });


    function switchTheme(themeName) {
        if (themeName == 'classic') {
            $('#bs-css').attr('href', 'assets/dashboard/bower_components/bootstrap/dist/css/bootstrap-theme.min.css');
        } else {
            $('#bs-css').attr('href', 'assets/dashboard/css/bootstrap-simplex.min.css');
        }

        $('#themes i').removeClass('glyphicon glyphicon-ok whitespace').addClass('whitespace');
        $('#themes a[data-value=' + themeName + ']').find('i').removeClass('whitespace').addClass('glyphicon glyphicon-ok');
    }

    //ajax menu checkbox
    $('#is-ajax').click(function (e) {
        $.cookie('is-ajax', $(this).prop('checked'), {
            expires: 365
        });
    });
    $('#is-ajax').prop('checked', $.cookie('is-ajax') === 'true' ? true : false);

    //disbaling some functions for Internet Explorer
    if (msie) {
        $('#is-ajax').prop('checked', false);
        $('#for-is-ajax').hide();
        $('#toggle-fullscreen').hide();
        $('.login-box').find('.input-large').removeClass('span10');

    }


    //highlight current / active link
    $('ul.main-menu li a').each(function () {
        if ($($(this))[0].href == String(window.location))
            $(this).parent().addClass('active');
    });

    //establish history variables
    var
        History = window.History, // Note: We are using a capital H instead of a lower h
        State = History.getState(),
        $log = $('#log');

    //bind to State Change
    History.Adapter.bind(window, 'statechange', function () { // Note: We are using statechange instead of popstate
        var State = History.getState(); // Note: We are using History.getState() instead of event.state
        $.ajax({
            url: State.url,
            success: function (msg) {
                $('#content').html($(msg).find('#content').html());
                $('#loading').remove();
                $('#content').fadeIn();
                var newTitle = $(msg).filter('title').text();
                $('title').text(newTitle);
                docReady();
            }
        });
    });

    //ajaxify menus
    $('a.ajax-link').click(function (e) {
        if (msie) e.which = 1;
        if (e.which != 1 || !$('#is-ajax').prop('checked') || $(this).parent().hasClass('active')) return;
        e.preventDefault();
        $('.sidebar-nav').removeClass('active');
        $('.navbar-toggle').removeClass('active');
        $('#loading').remove();
        $('#content').fadeOut().parent().append('<div id="loading" class="center">Loading...<div class="center"></div></div>');
        var $clink = $(this);
        History.pushState(null, null, $clink.attr('href'));
        $('ul.main-menu li.active').removeClass('active');
        $clink.parent('li').addClass('active');
    });

    $('.accordion > a').click(function (e) {
        e.preventDefault();
        var $ul = $(this).siblings('ul');
        var $li = $(this).parent();
        if ($ul.is(':visible')) $li.removeClass('active');
        else $li.addClass('active');
        $ul.slideToggle();
    });

    $('.accordion li.active:first').parents('ul').slideDown();


    //other things to do on document ready, separated for ajax calls
    docReady();
});


function docReady() {
    //prevent # links from moving to top
    $('a[href="#"][data-top!=true]').click(function (e) {
        e.preventDefault();
    });

    //notifications
    $('.noty').click(function (e) {
        e.preventDefault();
        var options = $.parseJSON($(this).attr('data-noty-options'));
        noty(options);
    });

    //chosen - improves select
    $('[data-rel="chosen"],[rel="chosen"]').chosen();

    //tabs
    $('#myTab a:first').tab('show');
    $('#myTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });


    //tooltip
    $('[data-toggle="tooltip"]').tooltip();

    //auto grow textarea
    $('textarea.autogrow').autogrow();

    //popover
    $('[data-toggle="popover"]').popover();

    //iOS / iPhone style toggle switch
    $('.iphone-toggle').iphoneStyle();

    //star rating
    $('.raty').raty({
        score: 4 //default stars
    });

    //uploadify - multiple uploads
    $('#file_upload').uploadify({
        'swf': 'misc/uploadify.swf',
        'uploader': 'misc/uploadify.php'
        // Put your options here
    });

    //gallery controls container animation
    $('ul.gallery li').hover(function () {
        $('img', this).fadeToggle(1000);
        $(this).find('.gallery-controls').remove();
        $(this).append('<div class="well gallery-controls">' +
            '<p><a href="#" class="gallery-edit btn"><i class="glyphicon glyphicon-edit"></i></a> <a href="#" class="gallery-delete btn"><i class="glyphicon glyphicon-remove"></i></a></p>' +
            '</div>');
        $(this).find('.gallery-controls').stop().animate({
            'margin-top': '-1'
        }, 400);
    }, function () {
        $('img', this).fadeToggle(1000);
        $(this).find('.gallery-controls').stop().animate({
            'margin-top': '-30'
        }, 200, function () {
            $(this).remove();
        });
    });


    //gallery image controls example
    //gallery delete
    $('.thumbnails').on('click', '.gallery-delete', function (e) {
        e.preventDefault();
        //get image id
        //alert($(this).parents('.thumbnail').attr('id'));
        $(this).parents('.thumbnail').fadeOut();
    });
    //gallery edit
    $('.thumbnails').on('click', '.gallery-edit', function (e) {
        e.preventDefault();
        //get image id
        //alert($(this).parents('.thumbnail').attr('id'));
    });

    //gallery colorbox
    $('.thumbnail a').colorbox({
        rel: 'thumbnail a',
        transition: "elastic",
        maxWidth: "95%",
        maxHeight: "95%",
        slideshow: true
    });

    //gallery fullscreen
    $('#toggle-fullscreen').button().click(function () {
        var button = $(this),
            root = document.documentElement;
        if (!button.hasClass('active')) {
            $('#thumbnails').addClass('modal-fullscreen');
            if (root.webkitRequestFullScreen) {
                root.webkitRequestFullScreen(
                    window.Element.ALLOW_KEYBOARD_INPUT
                );
            } else if (root.mozRequestFullScreen) {
                root.mozRequestFullScreen();
            }
        } else {
            $('#thumbnails').removeClass('modal-fullscreen');
            (document.webkitCancelFullScreen ||
                document.mozCancelFullScreen ||
                $.noop).apply(document);
        }
    });

    //tour
    if ($('.tour').length && typeof (tour) == 'undefined') {
        var tour = new Tour();
        tour.addStep({
            element: "#content",
            /* html element next to which the step popover should be shown */
            placement: "top",
            title: "Custom Tour",
            /* title of the popover */
            content: "You can create tour like this. Click Next." /* content of the popover */
        });
        tour.addStep({
            element: ".theme-container",
            placement: "left",
            title: "Themes",
            content: "You change your theme from here."
        });
        tour.addStep({
            element: "ul.main-menu a:first",
            title: "Dashboard",
            content: "This is your dashboard from here you will find highlights."
        });
        tour.addStep({
            element: "#for-is-ajax",
            title: "Ajax",
            content: "You can change if pages load with Ajax or not."
        });
        tour.addStep({
            element: ".top-nav a:first",
            placement: "bottom",
            title: "Visit Site",
            content: "Visit your front end from here."
        });

        tour.restart();
    }

    //datatable
    $('.datatable').dataTable({
        "sDom": "<'row'<'col-md-5'l><'col-md-5'f><'btns col-md-2'>r>t<'row'<'col-md-10'i><'btns col-md-2'>><'col-md-12 center-block'p>",
        "sPaginationType": "bootstrap",
        "oLanguage": {
            "sLengthMenu": "_MENU_ records per page"
        },initComplete: function () {
            api = this.api();
        }
        //        initComplete: function () {
        //            var api = this.api();
        // 
        //           // var app = $('.datatable').api();
        //            console.log(this);
        //            console.log($('.datatable'));
        //            
        //            api.columns().indexes().flatten().each( function ( i ) {
        //                var column = api.column( i );
        //                var select = $('<select><option value=""></option></select>')
        //                    .appendTo( $(column.footer()).empty() )
        //                    .on( 'change', function () {
        //                        var val = $.fn.dataTable.util.escapeRegex(
        //                            $(this).val()
        //                        );
        // 
        //                        column
        //                            .search( val ? '^'+val+'$' : '', true, false )
        //                            .draw();
        //                    } );
        // 
        //                column.data().unique().sort().each( function ( d, j ) {
        //                    select.append( '<option value="'+d+'">'+d+'</option>' )
        //                } );
        //            } );
        //        }
    });



    $('.btn-close').click(function (e) {
        e.preventDefault();
        $(this).parent().parent().parent().fadeOut();
    });
    $('.btn-minimize').click(function (e) {
        e.preventDefault();
        var $target = $(this).parent().parent().next('.box-content');
        if ($target.is(':visible')) $('i', $(this)).removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
        else $('i', $(this)).removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
        $target.slideToggle();
    });
    $('.btn-setting').click(function (e) {
        e.preventDefault();
        $('#myModal').modal('show');
    });

}


//additional functions for data table
$.fn.dataTableExt.oApi.fnPagingInfo = function (oSettings) {
    return {
        "iStart": oSettings._iDisplayStart,
        "iEnd": oSettings.fnDisplayEnd(),
        "iLength": oSettings._iDisplayLength,
        "iTotal": oSettings.fnRecordsTotal(),
        "iFilteredTotal": oSettings.fnRecordsDisplay(),
        "iPage": Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength),
        "iTotalPages": Math.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength)
    };
}
$.extend($.fn.dataTableExt.oPagination, {
    "bootstrap": {
        "fnInit": function (oSettings, nPaging, fnDraw) {
            var oLang = oSettings.oLanguage.oPaginate;
            var fnClickHandler = function (e) {
                e.preventDefault();
                if (oSettings.oApi._fnPageChange(oSettings, e.data.action)) {
                    fnDraw(oSettings);
                }
            };

            $(nPaging).addClass('pagination').append(
                '<ul class="pagination">' +
                '<li class="prev disabled"><a href="#">&larr; ' + oLang.sPrevious + '</a></li>' +
                '<li class="next disabled"><a href="#">' + oLang.sNext + ' &rarr; </a></li>' +
                '</ul>'
            );
            var els = $('a', nPaging);
            $(els[0]).bind('click.DT', {
                action: "previous"
            }, fnClickHandler);
            $(els[1]).bind('click.DT', {
                action: "next"
            }, fnClickHandler);
        },

        "fnUpdate": function (oSettings, fnDraw) {
            var iListLength = 5;
            var oPaging = oSettings.oInstance.fnPagingInfo();
            var an = oSettings.aanFeatures.p;
            var i, j, sClass, iStart, iEnd, iHalf = Math.floor(iListLength / 2);

            if (oPaging.iTotalPages < iListLength) {
                iStart = 1;
                iEnd = oPaging.iTotalPages;
            } else if (oPaging.iPage <= iHalf) {
                iStart = 1;
                iEnd = iListLength;
            } else if (oPaging.iPage >= (oPaging.iTotalPages - iHalf)) {
                iStart = oPaging.iTotalPages - iListLength + 1;
                iEnd = oPaging.iTotalPages;
            } else {
                iStart = oPaging.iPage - iHalf + 1;
                iEnd = iStart + iListLength - 1;
            }

            for (i = 0, iLen = an.length; i < iLen; i++) {
                // remove the middle elements
                $('li:gt(0)', an[i]).filter(':not(:last)').remove();

                // add the new list items and their event handlers
                for (j = iStart; j <= iEnd; j++) {
                    sClass = (j == oPaging.iPage + 1) ? 'class="active"' : '';
                    $('<li ' + sClass + '><a href="#">' + j + '</a></li>')
                        .insertBefore($('li:last', an[i])[0])
                        .bind('click', function (e) {
                            e.preventDefault();
                            oSettings._iDisplayStart = (parseInt($('a', this).text(), 10) - 1) * oPaging.iLength;
                            fnDraw(oSettings);
                        });
                }

                // add / remove disabled classes from the static elements
                if (oPaging.iPage === 0) {
                    $('li:first', an[i]).addClass('disabled');
                } else {
                    $('li:first', an[i]).removeClass('disabled');
                }

                if (oPaging.iPage === oPaging.iTotalPages - 1 || oPaging.iTotalPages === 0) {
                    $('li:last', an[i]).addClass('disabled');
                } else {
                    $('li:last', an[i]).removeClass('disabled');
                }
            }
        }
    }
});

var isItem = 0;
//for all pages' delete confirmation
$(document).on("click", ".itemDelete", function () {
    if ("Cancel" == $(".itemDelete").text()) {
        $(".infoModal").modal('toggle');

    } else {
        $("#confirmDialog").modal({
            backdrop: false
        });
        isItem = 1;
    }
});
$(document).on("click", ".btndelete", function () {
    $("#confirmDialog").modal({
        backdrop: false
    });
});
$(document).on("click", ".confirmDelete", function () {
    if (1 == isItem) {
        if (0 != $('.datatable').DataTable().row('.edited').length) {
            $('.datatable').DataTable().row('.edited').remove().draw(false);
            deleteEditedItem();
            isItem = 0;
        } else {
            $("#errorDialog").modal("toggle");
            $('.errorMsg').text('Please choose items you want to delete!');
        }
    } else {
        if (0 != $('.datatable').DataTable().row('.selected').length) {
            $('.datatable').DataTable().row('.selected').remove().draw(false);
            deleteSelectedItems();
        } else {
            $("#errorDialog").modal("toggle");
            $('.errorMsg').text('Please choose items you want to delete!');
        }
    }
});
$(document).on("click", ".confirmCancel", function () {
    isItem = 0;
});

$(document).on("click", "#addDates", function () {
    $("#dates").append($("#datepicker").val() + " ");
    if ('' != $("#datepicker").val())
        dates.push($("#datepicker").val());
});
$(document).on("click", "#clearDates", function () {
    $("#dates").text("");
    dates = [];
});

$(document).on("click", "#addKeyPoint", function () {
    if (6 > keyPointNum) {
        $("#keyPoints").append($("#keyPoint").val() + " ");
        if ('' != $("#keyPoint").val()) {
            keyPointNum++;
            keyPoints.push($("#keyPoint").val());
        }
    } else {
        //        $("#errorDialog").modal("toggle");
        //        $('.errorMsg').text('Course can only have six key words!');
        alert("Course can only have six key words!");
    }
    $("#keyPoint").val('');
});
$(document).on("click", "#clearKeyPoint", function () {
    $("#keyPoints").text("");
    keyPoints = [];
    keyPointNum = 0;
});

function initSearchBox() {

    // Setup - add a text input to each footer cell
        $('.datatable tfoot th').each(function () {
            var title = $('.datatable thead th').eq($(this).index()).text();
            $(this).html('<input placeholder="' + title + '" />');
        });

    // DataTable
    var table = $('.datatable').DataTable();

    // Apply the search
    table.columns().eq(0).each(function (colIdx) {
        $('input', table.column(colIdx).footer()).on('keyup change', function () {
            table
                .column(colIdx)
                .search(this.value)
                .draw();
        });
    });
}

function initSearchSelect() {

    api.columns().indexes().flatten().each(function (i) {
        var column = api.column(i);
        var select = $('<select style="width:'+$(column.footer()).empty().width()+'px;"><option value=""></option></select>')
            .appendTo($(column.footer()).empty())
            .on('change', function () {
                var val = $.fn.dataTable.util.escapeRegex(
                    $(this).val()
                );

                column
                    .search(val ? '^' + val + '$' : '', true, false)
                    .draw();
            });

        column.data().unique().sort().each(function (d, j) {
            select.append('<option value="' + d + '">' + d + '</option>')
        });
    });

    $('.datatable tfoot th').eq(0).html('<input type="checkbox" class="chkAll">');
    $('.datatable tfoot th').eq(1).html('')
    $('.datatable tfoot th').eq($('.datatable tfoot th').length - 1).html('')
    $('.datatable tfoot th').eq($('.datatable tfoot th').length - 2).html('')
}

//get parameters from url
function getParameter() {
    var url = document.URL;
    var para = new Array();
    if (url.lastIndexOf("?") > 0) {
        var temp = url.substring(url.lastIndexOf("?") + 1, url.length);
        var arr = temp.split("&");

        for (var i = 0; i < arr.length; i++) {
            var key = arr[i].split("=")[0];
            var vaule = arr[i].split("=")[1];
            var o={};
            o[key]=vaule;
            para.push(o);
        }
    }
    return para;
}
function addADBtn()
{
var html = "<a class='btn btn-info btnadd'><i class='glyphicon glyphicon-edit icon-white'></i>add</a> <a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
$(".btns").append(html);
}

function addDBtn()
{
var html = "<a class='btn btn-danger btndelete'><i class='glyphicon glyphicon-trash icon-white'></i>Delete</a>";
$(".btns").append(html);
}


function errorMsg(msg)
{
    $("#errorDialog").modal("toggle");
    $('.errorMsg').text(msg);    
}

function errorMsgAlert(msg)
{
    alert(msg);
}