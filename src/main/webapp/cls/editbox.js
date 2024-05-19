function rand(max) {
  return Math.floor(Math.random() * max);
}
// generera telefonsamtal
var intervalId = window.setInterval(function(){
    if(rand(10) === 2){
        console.log("phone call");
        
        // play phonecall sound
        document.getElementById('phone').play();
    }
}, 5000);
// "edit" knappen
// has to e rewritten


function editbtn(id){
    // ta förra värdet från kaka
    $("#form-"+id.toString()).toggle(500);
    $("#form-"+id.toString()).css('display', 'grid');
    

    document.cookie = "selord="+id;

}
function showbtn(id){ // general version of editbtn
    // ta förra värdet från kaka
    $("#box-"+id.toString()).toggle(500);
    $("#box-"+id.toString()).css('display', 'grid');
    

    document.cookie = "selord="+id;

}

function answerphone(id){
    var phone =  document.getElementById('phone');
    switch(id){
        case "busy":
            phone.pause();
            phone.currentTime = 0;;
            break;
    }
}
// set ordernumber cookie on form submission
function setCookie(key, value){
    document.cookie = ""+key+"="+value;
}