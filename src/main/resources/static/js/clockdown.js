$(".clock").TimeCircles(

    { time:
           { Days: { show: false },
            Hours: { show: false }}});
    $(".restart").click(function(){ $(".clock").TimeCircles().restart(); });