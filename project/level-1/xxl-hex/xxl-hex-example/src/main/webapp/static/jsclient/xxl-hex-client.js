/**
 *
 * dependency jquery
 * Created by xuxueli on 16/10/5.
 */

// --------------------------------- hex util ---------------------------------
var HexClient = {
    handle: function(BASE_URL, mapping, request_data){
        // data 2 json
        var request_json = JSON.stringify( request_data );

        // json 2 byte
        var request_byte = unicodeStrToUtf8Byte(request_json);
        len = get4Len(request_byte.length);

        var requsetWriter = new ByteWriteFactory();
        requsetWriter.writeInt(len);
        requsetWriter.writeByte(request_byte, len)

        var final_request_byte = requsetWriter.utf8ByteData;

        // byte 2 hex
        var request_hex = bytesToHex4Utf8(final_request_byte);

        var url = BASE_URL + "?mapping=" + mapping + "&hex=" + request_hex;
        var response_hex;
        $.ajax({
            url: url,
            dataType: 'text',
            async: false,
            success : function (data) {
                response_hex =  data;
                console.log("request_hex:" + request_hex);
                console.log("response_hex:" + response_hex);
                if (response_hex) {
                    response_hex = response_hex.trim();
                }
            }
        });
        if (response_hex) {
            // hex 2 byte
            var responseReader = new ByteReadFactory();
            responseReader.init(response_hex)

            // byte 2 json
            var resp_len = responseReader.readInt();
            var response_json = responseReader.readString(resp_len);
            response_json = response_json.trim();

            if (response_json && response_json.indexOf("{")>-1) {
                var response = JSON.parse( response_json );
                return response;
            }
        }
        return {'code':500, 'msg':'请求失败'};
    },
    handlePlain: function(BASE_URL, mapping, request_data){
        var url = BASE_URL + "?mapping=" + mapping + "&plain=" + true;
        var response_json;
        $.ajax({
            url: url,
            data: request_data,
            dataType: 'json',
            async: false,
            success : function (data) {
                response_json = data;
            }
        });
        if (response_json) {
            return response_json;
        }
        return {'code':500, 'msg':'请求失败'};
    }
}

// --------------------------------- ByteWriteFactory ---------------------------------
function ByteWriteFactory(){
    this.utf8ByteData  = new Array();
    this.writeInt = function (intValue) {
        var intBytes = new Array(4);
        for (var index = 0; index < 4; index++) {
            intBytes[index] = intValue >>> (index * 8);
        }
        this.utf8ByteData = this.utf8ByteData.concat(intBytes);
    };
    this.writeByte = function (utf8Bytes, length) {
        if (utf8Bytes.length < length){
            for (var i=utf8Bytes.length; i< length; i++){
                utf8Bytes.push(0x00);
            }
        }
        this.utf8ByteData = this.utf8ByteData.concat(utf8Bytes);
    };
    this.writeString = function (unicodeStr, length) {
        var utf8Bytes = unicodeStrToUtf8Byte(unicodeStr);
        if (utf8Bytes.length < length){
            for (var i=utf8Bytes.length; i< length; i++){
                utf8Bytes.push(0x00);
            }
        }
        this.utf8ByteData = this.utf8ByteData.concat(utf8Bytes);
    }
}

// --------------------------------- ByteReadFactory ---------------------------------
function ByteReadFactory() {
    this.offset = 0;
    this.response_byte;
    this.init = function (response_hex) {
        this.offset = 0;
        this.response_byte = hexToBytes4Utf8(response_hex);
    }
    this.readInt = function () {
        if (this.offset + 4 > this.response_byte.length) {
            return 0;
        }
        var intValue = (this.response_byte[this.offset] & 0xff)
            | ((this.response_byte[this.offset + 1] & 0xff) << 8)
            | ((this.response_byte[this.offset + 2] & 0xff) << 16)
            | ((this.response_byte[this.offset + 3] & 0xff) << 24);
        this.offset += 4;
        return intValue;
    }
    this.readString = function (length) {
        if (this.offset + length > this.response_byte.length) {
            console.log("[byte stream factory read string length error.]");
            return "";
        }

        var resultByte = new Array();
        for (var i = 0; i < length; i++) {
            var charCode = this.response_byte[this.offset + i];
            if (charCode > 0) {
                // var item = String.fromCharCode(charCode);
                resultByte.push(this.response_byte[this.offset + i]);
            }
        }

        //var result = String.fromCharCode.apply(null, resultByte);
        var result = utf8ByteToUnicodeStr(resultByte);
        this.offset += length;

        return result;
    }
}

// --------------------------------- len util ---------------------------------

/**
 * get len
 *
 * @param len
 * @returns {*}
 */
function get4Len(len) {
    if (len % 4 != 0) {
        // Length is best in multiples of four
        len = (Math.floor(len/4) + 1) * 4;
    }
    return len;
}

// --------------------------------- str(unicode)-byte(utf8)- util ---------------------------------
function unicodeStrToUtf8Byte(unicodeStr) {
    var utf8Bytes = [];
    for (var i = 0; i < unicodeStr.length; i++) {
        var charcode = unicodeStr.charCodeAt(i);

        if ((charcode >= 0x0) && (charcode <= 0x7F)) {
            utf8Bytes.push(charcode);
        } else if ((charcode >= 0x80) && (charcode <= 0x7FF)){
            utf8Bytes.push(0xc0 | ((charcode >> 6) & 0x1F));
            utf8Bytes.push(0x80 | (charcode & 0x3F));
        } else if ((charcode >= 0x800) && (charcode <= 0xFFFF)){
            utf8Bytes.push(0xe0 | ((charcode >> 12) & 0xF));
            utf8Bytes.push(0x80 | ((charcode >> 6) & 0x3F));
            utf8Bytes.push(0x80 | (charcode & 0x3F));
        } else if ((charcode >= 0x10000) && (charcode <= 0x1FFFFF)){
            utf8Bytes.push(0xF0 | ((charcode >> 18) & 0x7));
            utf8Bytes.push(0x80 | ((charcode >> 12) & 0x3F));
            utf8Bytes.push(0x80 | ((charcode >> 6) & 0x3F));
            utf8Bytes.push(0x80 | (charcode & 0x3F));
        } else if ((charcode >= 0x200000) && (charcode <= 0x3FFFFFF)){
            utf8Bytes.push(0xF8 | ((charcode >> 24) & 0x3));
            utf8Bytes.push(0x80 | ((charcode >> 18) & 0x3F));
            utf8Bytes.push(0x80 | ((charcode >> 12) & 0x3F));
            utf8Bytes.push(0x80 | ((charcode >> 6) & 0x3F));
            utf8Bytes.push(0x80 | (charcode & 0x3F));
        } else if ((charcode >= 0x4000000) && (charcode <= 0x7FFFFFFF)){
            utf8Bytes.push(0xFC | ((charcode >> 30) & 0x1));
            utf8Bytes.push(0x80 | ((charcode >> 24) & 0x3F));
            utf8Bytes.push(0x80 | ((charcode >> 18) & 0x3F));
            utf8Bytes.push(0x80 | ((charcode >> 12) & 0x3F));
            utf8Bytes.push(0x80 | ((charcode >> 6 ) & 0x3F));
            utf8Bytes.push(0x80 | (charcode & 0x3F));
        }
    }
    return utf8Bytes;
}

/**
 * utf8 byte to unicode string
 * @param utf8Bytes
 * @returns {string}
 */
function utf8ByteToUnicodeStr(utf8Bytes){
    var unicodeStr ="";
    for (var pos = 0; pos < utf8Bytes.length;){
        var flag= utf8Bytes[pos];
        var unicode = 0 ;
        if ((flag >>>7) === 0 ) {
            unicodeStr+= String.fromCharCode(utf8Bytes[pos]);
            pos += 1;

        } else if ((flag &0xFC) === 0xFC ){
            unicode = (utf8Bytes[pos] & 0x3) << 30;
            unicode |= (utf8Bytes[pos+1] & 0x3F) << 24;
            unicode |= (utf8Bytes[pos+2] & 0x3F) << 18;
            unicode |= (utf8Bytes[pos+3] & 0x3F) << 12;
            unicode |= (utf8Bytes[pos+4] & 0x3F) << 6;
            unicode |= (utf8Bytes[pos+5] & 0x3F);
            unicodeStr+= String.fromCharCode(unicode) ;
            pos += 6;

        }else if ((flag &0xF8) === 0xF8 ){
            unicode = (utf8Bytes[pos] & 0x7) << 24;
            unicode |= (utf8Bytes[pos+1] & 0x3F) << 18;
            unicode |= (utf8Bytes[pos+2] & 0x3F) << 12;
            unicode |= (utf8Bytes[pos+3] & 0x3F) << 6;
            unicode |= (utf8Bytes[pos+4] & 0x3F);
            unicodeStr+= String.fromCharCode(unicode) ;
            pos += 5;

        } else if ((flag &0xF0) === 0xF0 ){
            unicode = (utf8Bytes[pos] & 0xF) << 18;
            unicode |= (utf8Bytes[pos+1] & 0x3F) << 12;
            unicode |= (utf8Bytes[pos+2] & 0x3F) << 6;
            unicode |= (utf8Bytes[pos+3] & 0x3F);
            unicodeStr+= String.fromCharCode(unicode) ;
            pos += 4;

        } else if ((flag &0xE0) === 0xE0 ){
            unicode = (utf8Bytes[pos] & 0x1F) << 12;;
            unicode |= (utf8Bytes[pos+1] & 0x3F) << 6;
            unicode |= (utf8Bytes[pos+2] & 0x3F);
            unicodeStr+= String.fromCharCode(unicode) ;
            pos += 3;

        } else if ((flag &0xC0) === 0xC0 ){ //110
            unicode = (utf8Bytes[pos] & 0x3F) << 6;
            unicode |= (utf8Bytes[pos+1] & 0x3F);
            unicodeStr+= String.fromCharCode(unicode) ;
            pos += 2;

        } else{
            unicodeStr+= String.fromCharCode(utf8Bytes[pos]);
            pos += 1;
        }
    }
    return unicodeStr;
}

// --------------------------------- byte-hex(utf8) util ---------------------------------
/**
 * unicode-str (#unicodeStrToUtf8Byte) >>> utf8-byte (#bytesToHex4Utf8) >>> hex-utf8
 * unicode-str (#utf8ByteToUnicodeStr) <<< utf8-byte (#hexToBytes4Utf8) <<< hex-utf8
 */

var hex_tables = "0123456789ABCDEF";
function bytesToHex4Utf8(bytes) {
    for (var hex = [], i = 0; i < bytes.length; i++) {
        hex.push(hex_tables.charAt((bytes[i] & 0xf0) >> 4));
        hex.push(hex_tables.charAt((bytes[i] & 0x0f) >> 0));
    }
    return hex.join("");
}
function hexToBytes4Utf8(hex) {
    for (var bytes = [], c = 0; c < hex.length; c += 2)
        bytes.push(parseInt(hex.substr(c, 2), 16));
    return bytes;
}