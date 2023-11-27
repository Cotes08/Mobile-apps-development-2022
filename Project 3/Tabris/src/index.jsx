import {TextView, Button, contentView,TextInput, CheckBox, Printer} from 'tabris';


let count = 0;

let txt = <TextView text='Contador: 0' alignment='centerX' left={160} top={50} />;
contentView.append(txt);

contentView.append(
  <CheckBox left= {250} top={290} id='Negativos' text='Negativos'/>
);


contentView.append(
  <Button onSelect={countUp} left={75} top={130}>+</Button>
);

contentView.append(
  <Button onSelect={countDownd} left={235} top={130}>-</Button>
);

function countUp() {
  let aux2= $(CheckBox).only('#Negativos').checked ? 'yes' : 'no'
  txt.set({text: `Contador: ${++count}`});
  if (aux2=='no' && count<0) {
    txt.set({text: `Contador: ${count = 0}`})
  }
}

function countDownd() {  
  let aux= $(CheckBox).only('#Negativos').checked ? 'yes' : 'no'
  if(count<=0 && aux=='no')
    txt.set({text: `Contador: ${count = 0}`});    
  else{
    txt.set({text: `Contador: ${count--}`});
  }
}

contentView.append(
  <TextInput top={250} left='5%' right='65%'
      message='Contador'
      id='contador'
      keyboard='number'
      style='underline'/>
);

contentView.append(
  <Button onTap={resetCont} left= {24} top={305}>REINICIAR</Button>
);

function resetCont()
{
  let aux2= $(CheckBox).only('#Negativos').checked ? 'yes' : 'no'
  let aux= $(TextInput).only('#contador').text
  txt.set({text: `Contador: ${count = aux}`})
  if (aux2=='no' && count<0) {
    txt.set({text: `Contador: ${count = 0}`})
  }
}







  

    

