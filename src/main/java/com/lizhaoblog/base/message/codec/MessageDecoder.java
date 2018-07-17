/*
 * Copyright (C), 2015-2018
 * FileName: MessageDecoder
 * Author:   zhao
 * Date:     2018/7/16 15:05
 * Description: 消息解码器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.lizhaoblog.base.message.codec;

import com.lizhaoblog.base.constant.ConstantValue;
import com.lizhaoblog.base.message.StringMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 〈一句话功能简述〉<br>
 * 〈消息解码器〉
 *
 * @author zhao
 * @date 2018/7/16 15:05
 * @since 1.0.1
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder {

  //判断传送客户端传送过来的数据是否按照协议传输，头部信息的大小应该是 int+int+int = 4+4+4 = 12
  private static final int HEADER_SIZE = 12;

  private int messageId;
  private int statusCode;
  private int length;

  private String body;

  /**
   * @param maxFrameLength      解码时，处理每个帧数据的最大长度
   * @param lengthFieldOffset   该帧数据中，存放该帧数据的长度的数据的起始位置
   * @param lengthFieldLength   记录该帧数据长度的字段本身的长度
   * @param lengthAdjustment    修改帧数据长度字段中定义的值，可以为负数
   * @param initialBytesToStrip 解析的时候需要跳过的字节数
   * @param failFast            为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
   */
  public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
          int initialBytesToStrip, boolean failFast) {
    super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
  }

  @Override
  protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    if (in == null) {
      return null;
    }
    if (in.readableBytes() < HEADER_SIZE) {
      throw new Exception("可读信息段比头部信息都小");
    }

    //注意在读的过程中，readIndex的指针也在移动
    messageId = in.readInt();
    statusCode = in.readInt();
    length = in.readInt();

    if (in.readableBytes() < length) {
      throw new Exception("body获取长度" + length + ",实际长度没有达到");
    }
    ByteBuf buf = in.readBytes(length);
    byte[] req = new byte[buf.readableBytes()];
    buf.readBytes(req);
    body = new String(req, ConstantValue.PROJECT_CHARSET);

    //    CustomMsg customMsg = new CustomMsg(type, flag, length, body);
    StringMessage stringMessage = StringMessage.create(length, messageId, statusCode, body);
    return stringMessage;
  }

}