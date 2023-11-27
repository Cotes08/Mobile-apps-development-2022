import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View, Image, TextInput, ScrollView, Button, Switch, Alert } from 'react-native';
import React, { useState } from 'react';


export default function App() {

  const [isEnabled, setIsEnabled] = useState(false);//se pone a false para que el botn este desactivado
  const [isPressed, setIsPressed] = useState(true);//Se pone a true para que el boton este desactivado
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const toggleSwitch = () => {

    if (!email || !password) {
      setIsEnabled(true);//Se pone a true para cuando intente darle le salte el error y no se cambie a false
      setIsPressed(true);//Se pone a true para cuando intente darle le salte el error y no se cambie a false

      Alert.alert('Datos introducidos incorrectos', "No has rellenado los campos");
    }
    else {
      if (email && password) {
        setIsPressed(previousState => !previousState)
      } else {
        setIsPressed(previousState => !previousState)
      }
    }

    setIsEnabled(previousState => !previousState)
  }



  return (
    <View style={styles.container}>

      <Text style={{ fontSize: 18, marginTop: 80, marginLeft: 55 }}>
        Bienvenido a Practica3 React Native
      </Text>

      <Image style={{ width: 100, height: 100, marginTop: 15, marginLeft: 150 }} source={require('./assets/icon.png')}></Image>

      <Text style={{ marginLeft: 55, marginBottom: 5 }}>Email: </Text>
      <TextInput style={{ height: 50, width: 200, borderColor: 'gray', borderWidth: 1, marginLeft: 90 }} placeholder="usuario@gmail.com" onChangeText={email => setEmail(email)} keyboardType="email-address" />

      <Text style={{ marginTop: 10, marginLeft: 55, marginBottom: 5 }}>Contraseña: </Text>
      <TextInput style={{ height: 50, width: 200, borderColor: 'gray', borderWidth: 1, marginLeft: 90, marginBottom: 30 }} placeholder="Contraseña1234" onChangeText={password => setPassword(password)} secureTextEntry={true} />

      <ScrollView style={{ width: 370 }}>
        <Text style={{ marginTop: 25, marginLeft: 30, marginBottom: 5 }}>Terminos del servicio:</Text>
        <Text style={{ marginLeft: 30, marginBottom: 5 }}>
          Lorem Ipsum is simply dummy text of the printing and typesetting industry.
          Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
          when an unknown printer took a galley of type and scrambled it to make a type specimen book.
          It has survived not only five centuries, but also the leap into electronic typesetting,
          remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages,
          and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.

          Contrary to popular belief, Lorem Ipsum is not simply random text.
          It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old.
          Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words,
          consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature,
          discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of "de Finibus Bonorum et Malorum"
          The Extremes of Good and Evil by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance.
          The first line of Lorem Ipsum, "Lorem ipsum dolor sit amet..", comes from a line in section 1.10.32
        </Text>
        <View style={styles.row}>
          <Text style={{ fontSize: 15, marginLeft: 30 }}>He leido y estoy de acuerdo</Text>
          <Switch style={{ marginRight: 200 }}
            trackColor={{ false: 'grey', true: '#2E86C1' }}
            thumbColor={isEnabled ? '#f4f3f4' : '#f4f3f4'}
            ios_backgroundColor='grey'
            onValueChange={toggleSwitch}
            value={isEnabled}></Switch>
        </View>
      </ScrollView>

      <Text style={{ marginBottom: 50 }}></Text>

      <Button disabled={isPressed} onPress={() => { alert('Bienvenido' + ' ' + email); }} title='Crear cuenta'></Button>

      <StatusBar style="auto" />
    </View>
  );
}



const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  row: {
    flex: 1,
    alignItems: "center",
    flexDirection: "row",
  },

});


