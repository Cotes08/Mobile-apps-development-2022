
document.addEventListener('deviceready', onDeviceReady, false);

function onDeviceReady() {
    checkConnection();
}


function checkConnection() {
    var networkState = navigator.connection.type;

    var states = {};
    states[Connection.UNKNOWN]  = 'Unknown connection';
    states[Connection.ETHERNET] = 'Ethernet connection';
    states[Connection.WIFI]     = 'WiFi connection';
    states[Connection.CELL_2G]  = 'Cell 2G connection';
    states[Connection.CELL_3G]  = 'Cell 3G connection';
    states[Connection.CELL_4G]  = 'Cell 4G connection';
    states[Connection.CELL]     = 'Cell generic connection';
    states[Connection.NONE]     = 'No network connection';

    alert('Connection type: ' + states[networkState]);

    
    var check1 = document.getElementById('wifi');
    var check2 = document.getElementById('2g');
    var check3 = document.getElementById('3g');
    var check4 = document.getElementById('4g');
    var check5 = document.getElementById('cell');
    var check6 = document.getElementById('none');
    var check7 = document.getElementById('ethernet');
    var check8 = document.getElementById('unknown');

    if (states[networkState] == 'WiFi connection') {
        
        check1.checked = true;
        check2.checked = false;
        check3.checked = false; 
        check4.checked = false;
        check5.checked = false;
        check6.checked = false;
        check7.checked = false;
        check8.checked = false;
    }
    else if(states[networkState] == 'Cell 2G connection')
    {
        check2.checked = true;
        check1.checked = false;
        check3.checked = false; 
        check4.checked = false;
        check5.checked = false;
        check6.checked = false;
        check7.checked = false;
        check8.checked = false;
    }
    else if(states[networkState] == 'Cell 3G connection')
    {
        check3.checked = true;
        check2.checked = false;
        check1.checked = false; 
        check4.checked = false;
        check5.checked = false;
        check6.checked = false;
        check7.checked = false;
        check8.checked = false;
    }
    else if(states[networkState] == 'Cell 4G connection')
    { 
        check4.checked = true;
        check2.checked = false;
        check3.checked = false; 
        check1.checked = false;
        check5.checked = false;
        check6.checked = false;
        check7.checked = false;
        check8.checked = false;
    }
    else if(states[networkState] == 'Cell generic connection')
    {
        check5.checked = true;
        check2.checked = false;
        check3.checked = false; 
        check4.checked = false;
        check1.checked = false;
        check6.checked = false;
        check7.checked = false;
        check8.checked = false;
    }
    else if(states[networkState] == 'No network connection')
    {  
        check6.checked = true;
        check2.checked = false;
        check3.checked = false; 
        check4.checked = false;
        check5.checked = false;
        check1.checked = false;
        check7.checked = false;
        check8.checked = false;
    }
    else if(states[networkState] == 'Ethernet connection')
    {
        check7.checked = true;
        check2.checked = false;
        check3.checked = false; 
        check4.checked = false;
        check5.checked = false;
        check6.checked = false;
        check1.checked = false;
        check8.checked = false;
    }
    else
    {
        check8.checked = true;
        check2.checked = false;
        check3.checked = false; 
        check4.checked = false;
        check5.checked = false;
        check6.checked = false;
        check7.checked = false;
        check1.checked = false;
    }
}