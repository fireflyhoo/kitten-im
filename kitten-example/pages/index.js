
import styles from './index.css';
import React, { useState } from 'react';


export default function() {

  var appKey = "10086";
  var token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJpbS15YXlhdGFvIiwiYXBwS2V5IjoiMTAwODYiLCJleHAiOjE1OTMzMjc2OTEsInVzZXJLZXkiOiJkaW5nZGluZzAifQ.Lx-launSRZYKdKpX6ukdhM-hZkjRaKsD0wbSJyaYaFU";
  const [ messages, setMessage ] = useState([]);
  var handleConn = e => {
    console.log("handleConn")
    setMessage([...messages, "handleConn"]);
     var ws = new WebSocket("ws://127.0.0.1:8899/socket-io?appKey="+appKey+"&token="+token);
                
       ws.onopen = function()
       {
          // Web Socket 已连接上，使用 send() 方法发送数据
          ws.send('{"type":7}');
          alert("数据发送中...");
       };
        
       ws.onmessage = function (evt) 
       { 
          var received_msg = evt.data;
          alert("数据已接收..." + received_msg);
          setMessage([...messages, "xx"]);
       };
        
       ws.onclose = function()
       { 
          // 关闭 websocket
          alert("连接已关闭..."); 
       };
  };

  var handleSendMessage = e =>{
  	console.log("send message")
  };


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
