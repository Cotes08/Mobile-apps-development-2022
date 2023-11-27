document.addEventListener('deviceready', onDeviceReady, false);

function onDeviceReady() {
    
    var modelo = document.getElementById('modelo');
    var apacheCordova = document.getElementById('verApache');
    var plataforma = document.getElementById('plat');
    var version = document.getElementById('verPlat');
    var uuid = document.getElementById('uuid');

    modelo.value = device.model;
    apacheCordova.value = device.cordova;
    plataforma.value = device.platform;
    version.value = device.version;
    uuid.value = device.uuid;

}


