/*
 * Copyright (C), 2015-2018
 * FileName: IMessage
 * Author:   zhao
 * Date:     2018/7/12 10:53
 * Description: 消息的基类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lizhaoblog.base.message;

import com.lizhaoblog.base.exception.MessageCodecException;

/**
 * 〈一句话功能简述〉<br>
 * 〈消息的基类〉
 *
 * @author zhao
 * @date 2018/7/12 10:53
 * @since 1.0.1
 */
public interface IMessage {
  short getFlag();

  void setFlag(short flag);

  short getMessageId();

  void setMessageId(short messageId);

  short getStatusCode();

  void setStatusCode(short statusCode);

  int getLength();

  void setLength(int length);

  byte[] getBodyByte();

  void setBodyByte(byte[] body) throws MessageCodecException;

}
