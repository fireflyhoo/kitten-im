var KittenIMClient = (function () {
        function KittenIMClient() {
        }

        KittenIMClient.getInstance = function () {
            if (!KittenIMClient._instance) {
                throw new Error("KittenIMClient is not initialized. Call .init() method first.");
            }
            return KittenIMClient._instance;
        };

        KittenIMClient.init = function(appKey,options,callback) {
            if(KittenIMClient._instance){
                return KittenIMClient._memoryStore.sdkInfo;
            }
            KittenIMClient._instance = new KittenIMClient()
            options = options || {};
            
            var tempStore = {
                appKey: appKey,
                token: "",
                callback: null,
                userKey: "",
                providerType: 1,
                deltaTime: 0,
                filterMessages: [],
                isSyncRemoteConverList: true,
                otherDevice: false,
                custStore: {},
                converStore: { latestMessage: {} },
                connectAckTime: 0,
                depend: options,
                notification: {}
            };
            var socket = {
                onMessage: null,
                StatusChanged: null
            };
            KittenIMClient._memoryStore = tempStore;
            KittenIMClient._dataAccessProvider = KittenIMClient.SocketTransportation(socket);
        }

        //消息类型
        var Type = {};
        Type[Type["UP_LINE"]=1] = "UP_LINE";
        Type[Type["DOWN_LINE"]=2] = "DOWN_LINE";
        Type[Type["PTP"]=3] = "PTP";
        Type[Type["GROUP"]=4] = "GROUP";
        Type[Type["ACK"]=5] = "ACK";
        Type[Type["HEARTBEAT"]=6] = "HEARTBEAT";
        Type[Type["ECHO"]=7] = "ECHO";
        Type[Type["CMD"]=7] = "CMD";

        KittenIMClient.Type = Type;




        /*****
            var config = {
                appkey: appkey,
                token: token,
                opts: opts
            };
            callback(_instance, userKey);
        */
        KittenIMClient.initApp = function(config, callback){
            KittenIMClient.init(config.appKey,config.opts, function(){
                var instance = KittenIMClient._instance;
                var error  = null;
                callback(error,instance);
            });
        };

        // 连接 全局只能调用一次
        KittenIMClient.connect = function(token,_callback,userKey,serverConf){
            var url = "";
            KittenIMClient._dataAccessProvider.createTransport(url);
        };

        // 从新连接
        KittenIMClient.reconnect = function(callback, config) {
            KittenIMClient._dataAccessProvider.reconnect();
            callback();
        };

         //断开连接
        KittenIMClient.prototype.disconnect = function(){
            KittenIMClient._dataAccessProvider.disconnect();
        };


        // 登出
        KittenIMClient.prototype.logout = function(){
            var data = {};
            data.appkey = this._memoryStore.appkey;
            data.sender = this._memoryStore.userKey;
            data.type = KittenIMClient.Type["DOWN_LINE"];
            KittenIMClient._dataAccessProvider.send(JSON.stringify(data));
        };

       


        // 获取历史消息 
        KittenIMClient.prototype.getHistoryMessages = function(userKey,start_timestamp,end_timestamp){
            var data = {};
            data.appkey = this._memoryStore.appkey;
            data.sender = this._memoryStore.userKey;
            data.type = KittenIMClient.Type["CMD"]
            data.body = {};
            data.body.start_timestamp = start_timestamp;
            data.body.end_timestamp = end_timestamp;
            data.body = JSON.stringify(body);
            KittenIMClient._dataAccessProvider.send(JSON.stringify(data));
        };

        // 发送单个消息
        KittenIMClient.prototype.sendSingleMessage = function(targetKey, content, sendMessageCallback){
            var data = {};
            data.appkey = this._memoryStore.appkey;
            data.sender = this._memoryStore.userKey;
            data.target = targetKey; 
            data.type = KittenIMClient.Type["PTP"];
            data.body = JSON.stringify(content);
            KittenIMClient._dataAccessProvider.send(JSON.stringify(data));
        };

        // 发送批量消息
        KittenIMClient.prototype.sendBatchMessage = function(targetKeys, content, sendMessageCallback){

        };


        // 发送群组消息
        KittenIMClient.prototype.sendGroupMessage = function(targetKey, content, sendMessageCallback){

        };

        // 发送回声消息
        KittenIMClient.prototype.sendEchoMessage = function(sendMessageCallback){

        };

        // 清理历史消息
        KittenIMClient.prototype.clearRemoteHistoryMessages = function(params, callback){

        };

        // 创建分组
        KittenIMClient.prototype.createGroup = function(name){

        };

        // 加入群组
        KittenIMClient.prototype.joinGroup = function(groupId, targetKey){

        };

        // 退出群组
        KittenIMClient.prototype.leave = function(groupId){

        }
        return KittenIMClient;
    })();

 (function (KittenIMClient){
    var SocketTransportation = (function () {
        function SocketTransportation(_socket){
            this.connected = false;
            this.isClose = false;
            this.queue = [];
            this.empty = new function;
            this._socket = _socket;
            return this;
        };

        SocketTransportation.prototype.createTransport = function(url){
            if(!url){
                throw new Error("URL can't be empty")
            }
            this.url = url;
            this.socket = new WebSocket(url);
            this.socket.binaryType = "arraybuffer";
            this.addEvent();
            return this.socket;
        };

        SocketTransportation.prototype.send = function (data){
            this.socket.send(data);
        };

        SocketTransportation.prototype.onClose = function(ev){
            var me = this;
            me.isClose = true;
            me.socket = this.empty;
            if(ev.code == 1006 && !this._status){
                me._socket.StatusChanged("NETWORK_UNAVAILABLE")
            }else{
                me._status = 0;
            }
        };

        /**
        * 消息内容
        */
        SocketTransportation.prototype.onData = function(data){
            this._socket.onMessage(data);
        };

        /**
        *  错误信息
        */
        SocketTransportation.prototype.onError = function(error){
            throw new Error(error);
        };

        /**
        *  注册事件
        */
        SocketTransportation.prototype.addEvent = function(){
            var self = this;
            self.socket.onopen = function(){
                self.connected = true;
                self.isClose = false;
                //通道可用后,吧所有队列中的消息一次性发出去
                self.doQueue();
                // 触发外部连接事件
            };

            self.socket.onmessage =function(ev){
                self.onData(ev.data);
            }

            self.socket.onerror  = function(ev){
                self.onError(ev);
            };

            self.socket.onclose = function(ev){
                self.onClose(ev);
            };
        };

        /**
        * 队列
        */
        SocketTransportation.prototype.doQueue = function(){
            var self = this;
            for (var i = 0, len = self.queue.length; i < len; i++) {
                self.send(self.queue[i]);
            }
        };

         /**
         * [disconnect 断开连接]
         */
        SocketTransportation.prototype.disconnect = function (status) {
            var me = this;
            if (me.socket.readyState) {
                me.isClose = true;
                if (status) {
                    me._status = status;
                }
                me.socket.close();
            }
        };

        /**
         * [reconnect 重新连接]
         */
        SocketTransportation.prototype.reconnect = function () {
            this.disconnect();
            this.createTransport(this.url);
        };

        /**
        * [close 关闭连接]
        */
        SocketTransportation.prototype.close = function () {
            this.socket.close();
        };

    })();
    KittenIMClient.SocketTransportation = SocketTransportation;
 })(KittenIMClient);

    

























