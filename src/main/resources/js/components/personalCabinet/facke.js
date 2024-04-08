var in_BG = false;

// SET   --->    IR.GetDevice("iRidium Server").Set("SIP.ANSWER", 0)  IR.GetDevice("SIP").Set("ANSWER", 0)
// GET   --->    IR.GetVariable("Drivers.iRidium Server.SIP.AUDIO VOLUME")
// SIP events handling
IR.AddListener(IR.EVENT_TAG_CHANGE, IR.GetDevice("iRidium Server"), function(name, value)
{
  if(name == 'SIP.INCOMING CALL')
  {
    if(value == 1)
    {


//                        IR.GetDevice("AV & Custom Systems (HTTP)").SendEx({                                 // .SendEx command as HTTP-text
//                            Type: "GET",
//                            Url: "/?key=-jl1j39k3ff9r7p-1b-cb2b3_m7p2vs2&title=You%20have%20not%20recived%20call&sound_type=1",
//                            cbReceiveText: function (text, code, headers) {
//                                IR.Log(text)
//                                IR.Log(code)
//                            },
//                            cbTimeOut: function () { IR.Log("cbTimeOut: TimeOut") },
//                        });
//

            IR.SendPush("Входящий звонок в домофон!")
      IR.ShowPopup('Incoming');
      IR.PlaySound("Ringing.wav", 0, 50, true);

      IR.Log("type of incomming call --->  " + IR.GetVariable("Drivers.iRidium Server.SIP.INCOMING CALL TYPE"))

      if(in_BG)
      {
        var number = IR.GetVariable("Drivers.iRidium Server.SIP.INCOMING CALL NUMBER");
        IR.SendNotification("Incoming call from " + number, 0, 2, 0, number);
      }
    }
    else
    {
      IR.HidePopup('Incoming');
      IR.StopSound("Ringing.wav");
    }
  }

});

// when the application goes to the background
IR.AddListener(IR.EVENT_APP_ENTER_BACKGROUND, 0, function()
{
  in_BG = true;
});

// when the application comes to the foreground
IR.AddListener(IR.EVENT_APP_ENTER_FOREGROUND, 0, function()
{
  in_BG = false;
});


// функция вызова абонента по указанному номеру
function call() {
  var text = IR.GetPopup("Домофон-Главная").GetItem("nameOfIdentifier").Text
  IR.SetVariable("Drivers.iRidium Server.SIP.NUMBER", text);
  IR.Log(IR.GetVariable("Drivers.iRidium Server.SIP.NUMBER"));

}


// функция перехода на попап разговора
function ToTalking() {

  var deleteListener;

  IR.AddListener(IR.EVENT_TAG_CHANGE, IR.GetDevice("SIP_Panel"), function(name, value)
  {
    IR.GetPopup("Talking").GetItem("Item 1").Visible = false;
    IR.GetPopup("Talking").GetItem("Item 4").Visible = false;

    if(name == 'STATUS') {

      if(value == 'Talking...')
      {
        IR.StopSound("Alerting.wav");
      } else if(value == 'On Hook...')
      {
        IR.StopSound("Alerting.wav");
        IR.HidePopup("Talking");
        IR.HidePopup("Outgoing");
      }

      IR.Log(value);
      if (value == "Talking...") {

        IR.HidePopup("Outgoing");
        IR.SetTimeout(500, function() {IR.ShowPopup("Talking");})


        deleteListener = true;


        // if (IR.GetVariable("Drivers.iRidium Server.SIP.NUMBER") == 100) {
          IR.GetPopup("Talking").GetItem("Item 1").Visible = true;
          IR.GetPopup("Talking").GetItem("Item 4").Visible = true;
        // }
      }
    }

    if (deleteListener == true) {
      IR.RemoveListener(IR.EVENT_TAG_CHANGE, IR.GetDevice("iRidium Server"), function(name, value) {})   // удаляем слушатель
      deleteListener = false;
    }

  })
}
