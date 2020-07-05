
import styles from './index.css';
import React, { useState } from 'react';

var ws  = null;
export default function() {

  var appKey = "10086";
  var token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJpbS15YXlhdGFvIiwiYXBwS2V5IjoiMTAwODYiLCJleHAiOjE1OTM4OTQ1ODgsInVzZXJLZXkiOiJkaW5nZGluZzAifQ.qYI4AhgfKvfGjPuXK0x6oqF7zZ2fxK-Kqkj4ccFwDaE";
  const [ messages, setMessage ] = useState([]);


  var handleConn = e => {
    console.log("handleConn")
    setMessage([...messages, "handleConn"]);
     ws = new WebSocket("ws://127.0.0.1:8899/socket-io?appKey="+appKey+"&token="+token);
                
       ws.onopen = function(){
          // Web Socket 已连接上，使用 send() 方法发送数据
          ws.send('{"type":7}');
       };
        
       ws.onmessage = function (evt){
       	  evt.data.text().then(text => {
       	  	console.log(text);
       	  });
       	  console.log("onMessage:",evt.data);
        
          
       };
        
       ws.onclose = function(e){
          console.log(e);
          // 关闭 websocket
          alert("连接已关闭..."); 
       };
  };

  var handleSendMessage = e =>{
    console.log(ws);
  	ws.send('{"type":7}')

  };

  function Uint8ArrayToString(fileData){
	  var dataString = "";
	  for (var i = 0; i < fileData.length; i++) {
	    dataString += String.fromCharCode(fileData[i]);
	  }
	  return dataString
  }


  return (
    <div className={styles.normal}>
      <h1>发送消息</h1>
      <div>
      		<ul className={styles.message_boards}>
            	<li><h3>消息内容</h3></li>
            	<li>
            		<div id="message_boards">
            			 {
						    messages.map(function (item) {
						      return <div key={item}>{item}!</div>
						    })
						  }
            		</div>
            	</li>
            </ul>
      		<ul className={styles.send_pand}>
	      		<li>
	            	<label>appKey:</label><input type="text"  name="appKey"/> <label>userKey:</label><input type="text"  name="userKey"/> <button onClick={handleConn}>连接</button>
	            </li>
	            <li>
                    <textarea id="msg" rows="10" cols="100"></textarea>
	            </li>
	            <li>
	            	<button onClick={handleSendMessage}>发送消息</button>
	            </li>
            </ul>

      </div>
    </div>
  );
}
