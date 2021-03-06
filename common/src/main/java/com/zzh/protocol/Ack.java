// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ack.proto

package com.zzh.protocol;

public final class Ack {
  private Ack() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface AckMsgOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.zzh.protocol.AckMsg)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * 消息唯一ID
     * </pre>
     *
     * <code>int64 id = 1;</code>
     */
    long getId();

    /**
     * <pre>
     *创建时间
     * </pre>
     *
     * <code>int64 timeStamp = 2;</code>
     */
    long getTimeStamp();

    /**
     * <code>.com.zzh.protocol.AckMsg.Module fromModule = 3;</code>
     */
    int getFromModuleValue();
    /**
     * <code>.com.zzh.protocol.AckMsg.Module fromModule = 3;</code>
     */
    com.zzh.protocol.Ack.AckMsg.Module getFromModule();

    /**
     * <pre>
     *此消息接收方的ID
     * </pre>
     *
     * <code>string destId = 4;</code>
     */
    java.lang.String getDestId();
    /**
     * <pre>
     *此消息接收方的ID
     * </pre>
     *
     * <code>string destId = 4;</code>
     */
    com.google.protobuf.ByteString
        getDestIdBytes();

    /**
     * <pre>
     *目标消息ID
     * </pre>
     *
     * <code>int64 ackMsgId = 5;</code>
     */
    long getAckMsgId();

    /**
     * <code>int64 ackMsgSessionId = 6;</code>
     */
    long getAckMsgSessionId();

    /**
     * <pre>
     *此消息发送方的id
     * </pre>
     *
     * <code>string fromId = 7;</code>
     */
    java.lang.String getFromId();
    /**
     * <pre>
     *此消息发送方的id
     * </pre>
     *
     * <code>string fromId = 7;</code>
     */
    com.google.protobuf.ByteString
        getFromIdBytes();
  }
  /**
   * <pre>
   *与ack相关的只有client和server
   *client&lt;-&gt;server:目标用户ID 目标消息ID
   * </pre>
   *
   * Protobuf type {@code com.zzh.protocol.AckMsg}
   */
  public  static final class AckMsg extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:com.zzh.protocol.AckMsg)
      AckMsgOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use AckMsg.newBuilder() to construct.
    private AckMsg(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private AckMsg() {
      fromModule_ = 0;
      destId_ = "";
      fromId_ = "";
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private AckMsg(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {

              id_ = input.readInt64();
              break;
            }
            case 16: {

              timeStamp_ = input.readInt64();
              break;
            }
            case 24: {
              int rawValue = input.readEnum();

              fromModule_ = rawValue;
              break;
            }
            case 34: {
              java.lang.String s = input.readStringRequireUtf8();

              destId_ = s;
              break;
            }
            case 40: {

              ackMsgId_ = input.readInt64();
              break;
            }
            case 48: {

              ackMsgSessionId_ = input.readInt64();
              break;
            }
            case 58: {
              java.lang.String s = input.readStringRequireUtf8();

              fromId_ = s;
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.zzh.protocol.Ack.internal_static_com_zzh_protocol_AckMsg_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.zzh.protocol.Ack.internal_static_com_zzh_protocol_AckMsg_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.zzh.protocol.Ack.AckMsg.class, com.zzh.protocol.Ack.AckMsg.Builder.class);
    }

    /**
     * Protobuf enum {@code com.zzh.protocol.AckMsg.Module}
     */
    public enum Module
        implements com.google.protobuf.ProtocolMessageEnum {
      /**
       * <code>CLIENT = 0;</code>
       */
      CLIENT(0),
      /**
       * <code>SERVER = 1;</code>
       */
      SERVER(1),
      /**
       * <code>TRANSFER = 2;</code>
       */
      TRANSFER(2),
      UNRECOGNIZED(-1),
      ;

      /**
       * <code>CLIENT = 0;</code>
       */
      public static final int CLIENT_VALUE = 0;
      /**
       * <code>SERVER = 1;</code>
       */
      public static final int SERVER_VALUE = 1;
      /**
       * <code>TRANSFER = 2;</code>
       */
      public static final int TRANSFER_VALUE = 2;


      public final int getNumber() {
        if (this == UNRECOGNIZED) {
          throw new java.lang.IllegalArgumentException(
              "Can't get the number of an unknown enum value.");
        }
        return value;
      }

      /**
       * @deprecated Use {@link #forNumber(int)} instead.
       */
      @java.lang.Deprecated
      public static Module valueOf(int value) {
        return forNumber(value);
      }

      public static Module forNumber(int value) {
        switch (value) {
          case 0: return CLIENT;
          case 1: return SERVER;
          case 2: return TRANSFER;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<Module>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static final com.google.protobuf.Internal.EnumLiteMap<
          Module> internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<Module>() {
              public Module findValueByNumber(int number) {
                return Module.forNumber(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(ordinal());
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return com.zzh.protocol.Ack.AckMsg.getDescriptor().getEnumTypes().get(0);
      }

      private static final Module[] VALUES = values();

      public static Module valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        if (desc.getIndex() == -1) {
          return UNRECOGNIZED;
        }
        return VALUES[desc.getIndex()];
      }

      private final int value;

      private Module(int value) {
        this.value = value;
      }

      // @@protoc_insertion_point(enum_scope:com.zzh.protocol.AckMsg.Module)
    }

    public static final int ID_FIELD_NUMBER = 1;
    private long id_;
    /**
     * <pre>
     * 消息唯一ID
     * </pre>
     *
     * <code>int64 id = 1;</code>
     */
    public long getId() {
      return id_;
    }

    public static final int TIMESTAMP_FIELD_NUMBER = 2;
    private long timeStamp_;
    /**
     * <pre>
     *创建时间
     * </pre>
     *
     * <code>int64 timeStamp = 2;</code>
     */
    public long getTimeStamp() {
      return timeStamp_;
    }

    public static final int FROMMODULE_FIELD_NUMBER = 3;
    private int fromModule_;
    /**
     * <code>.com.zzh.protocol.AckMsg.Module fromModule = 3;</code>
     */
    public int getFromModuleValue() {
      return fromModule_;
    }
    /**
     * <code>.com.zzh.protocol.AckMsg.Module fromModule = 3;</code>
     */
    public com.zzh.protocol.Ack.AckMsg.Module getFromModule() {
      @SuppressWarnings("deprecation")
      com.zzh.protocol.Ack.AckMsg.Module result = com.zzh.protocol.Ack.AckMsg.Module.valueOf(fromModule_);
      return result == null ? com.zzh.protocol.Ack.AckMsg.Module.UNRECOGNIZED : result;
    }

    public static final int DESTID_FIELD_NUMBER = 4;
    private volatile java.lang.Object destId_;
    /**
     * <pre>
     *此消息接收方的ID
     * </pre>
     *
     * <code>string destId = 4;</code>
     */
    public java.lang.String getDestId() {
      java.lang.Object ref = destId_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        destId_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *此消息接收方的ID
     * </pre>
     *
     * <code>string destId = 4;</code>
     */
    public com.google.protobuf.ByteString
        getDestIdBytes() {
      java.lang.Object ref = destId_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        destId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int ACKMSGID_FIELD_NUMBER = 5;
    private long ackMsgId_;
    /**
     * <pre>
     *目标消息ID
     * </pre>
     *
     * <code>int64 ackMsgId = 5;</code>
     */
    public long getAckMsgId() {
      return ackMsgId_;
    }

    public static final int ACKMSGSESSIONID_FIELD_NUMBER = 6;
    private long ackMsgSessionId_;
    /**
     * <code>int64 ackMsgSessionId = 6;</code>
     */
    public long getAckMsgSessionId() {
      return ackMsgSessionId_;
    }

    public static final int FROMID_FIELD_NUMBER = 7;
    private volatile java.lang.Object fromId_;
    /**
     * <pre>
     *此消息发送方的id
     * </pre>
     *
     * <code>string fromId = 7;</code>
     */
    public java.lang.String getFromId() {
      java.lang.Object ref = fromId_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        fromId_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *此消息发送方的id
     * </pre>
     *
     * <code>string fromId = 7;</code>
     */
    public com.google.protobuf.ByteString
        getFromIdBytes() {
      java.lang.Object ref = fromId_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        fromId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (id_ != 0L) {
        output.writeInt64(1, id_);
      }
      if (timeStamp_ != 0L) {
        output.writeInt64(2, timeStamp_);
      }
      if (fromModule_ != com.zzh.protocol.Ack.AckMsg.Module.CLIENT.getNumber()) {
        output.writeEnum(3, fromModule_);
      }
      if (!getDestIdBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 4, destId_);
      }
      if (ackMsgId_ != 0L) {
        output.writeInt64(5, ackMsgId_);
      }
      if (ackMsgSessionId_ != 0L) {
        output.writeInt64(6, ackMsgSessionId_);
      }
      if (!getFromIdBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 7, fromId_);
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (id_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(1, id_);
      }
      if (timeStamp_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, timeStamp_);
      }
      if (fromModule_ != com.zzh.protocol.Ack.AckMsg.Module.CLIENT.getNumber()) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(3, fromModule_);
      }
      if (!getDestIdBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, destId_);
      }
      if (ackMsgId_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(5, ackMsgId_);
      }
      if (ackMsgSessionId_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(6, ackMsgSessionId_);
      }
      if (!getFromIdBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(7, fromId_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.zzh.protocol.Ack.AckMsg)) {
        return super.equals(obj);
      }
      com.zzh.protocol.Ack.AckMsg other = (com.zzh.protocol.Ack.AckMsg) obj;

      if (getId()
          != other.getId()) return false;
      if (getTimeStamp()
          != other.getTimeStamp()) return false;
      if (fromModule_ != other.fromModule_) return false;
      if (!getDestId()
          .equals(other.getDestId())) return false;
      if (getAckMsgId()
          != other.getAckMsgId()) return false;
      if (getAckMsgSessionId()
          != other.getAckMsgSessionId()) return false;
      if (!getFromId()
          .equals(other.getFromId())) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getId());
      hash = (37 * hash) + TIMESTAMP_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getTimeStamp());
      hash = (37 * hash) + FROMMODULE_FIELD_NUMBER;
      hash = (53 * hash) + fromModule_;
      hash = (37 * hash) + DESTID_FIELD_NUMBER;
      hash = (53 * hash) + getDestId().hashCode();
      hash = (37 * hash) + ACKMSGID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getAckMsgId());
      hash = (37 * hash) + ACKMSGSESSIONID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getAckMsgSessionId());
      hash = (37 * hash) + FROMID_FIELD_NUMBER;
      hash = (53 * hash) + getFromId().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.zzh.protocol.Ack.AckMsg parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.zzh.protocol.Ack.AckMsg parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.zzh.protocol.Ack.AckMsg parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.zzh.protocol.Ack.AckMsg parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.zzh.protocol.Ack.AckMsg parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.zzh.protocol.Ack.AckMsg parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.zzh.protocol.Ack.AckMsg parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.zzh.protocol.Ack.AckMsg parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.zzh.protocol.Ack.AckMsg parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.zzh.protocol.Ack.AckMsg parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.zzh.protocol.Ack.AckMsg parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.zzh.protocol.Ack.AckMsg parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.zzh.protocol.Ack.AckMsg prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     *与ack相关的只有client和server
     *client&lt;-&gt;server:目标用户ID 目标消息ID
     * </pre>
     *
     * Protobuf type {@code com.zzh.protocol.AckMsg}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.zzh.protocol.AckMsg)
        com.zzh.protocol.Ack.AckMsgOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.zzh.protocol.Ack.internal_static_com_zzh_protocol_AckMsg_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.zzh.protocol.Ack.internal_static_com_zzh_protocol_AckMsg_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.zzh.protocol.Ack.AckMsg.class, com.zzh.protocol.Ack.AckMsg.Builder.class);
      }

      // Construct using com.zzh.protocol.Ack.AckMsg.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        id_ = 0L;

        timeStamp_ = 0L;

        fromModule_ = 0;

        destId_ = "";

        ackMsgId_ = 0L;

        ackMsgSessionId_ = 0L;

        fromId_ = "";

        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.zzh.protocol.Ack.internal_static_com_zzh_protocol_AckMsg_descriptor;
      }

      @java.lang.Override
      public com.zzh.protocol.Ack.AckMsg getDefaultInstanceForType() {
        return com.zzh.protocol.Ack.AckMsg.getDefaultInstance();
      }

      @java.lang.Override
      public com.zzh.protocol.Ack.AckMsg build() {
        com.zzh.protocol.Ack.AckMsg result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public com.zzh.protocol.Ack.AckMsg buildPartial() {
        com.zzh.protocol.Ack.AckMsg result = new com.zzh.protocol.Ack.AckMsg(this);
        result.id_ = id_;
        result.timeStamp_ = timeStamp_;
        result.fromModule_ = fromModule_;
        result.destId_ = destId_;
        result.ackMsgId_ = ackMsgId_;
        result.ackMsgSessionId_ = ackMsgSessionId_;
        result.fromId_ = fromId_;
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.zzh.protocol.Ack.AckMsg) {
          return mergeFrom((com.zzh.protocol.Ack.AckMsg)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.zzh.protocol.Ack.AckMsg other) {
        if (other == com.zzh.protocol.Ack.AckMsg.getDefaultInstance()) return this;
        if (other.getId() != 0L) {
          setId(other.getId());
        }
        if (other.getTimeStamp() != 0L) {
          setTimeStamp(other.getTimeStamp());
        }
        if (other.fromModule_ != 0) {
          setFromModuleValue(other.getFromModuleValue());
        }
        if (!other.getDestId().isEmpty()) {
          destId_ = other.destId_;
          onChanged();
        }
        if (other.getAckMsgId() != 0L) {
          setAckMsgId(other.getAckMsgId());
        }
        if (other.getAckMsgSessionId() != 0L) {
          setAckMsgSessionId(other.getAckMsgSessionId());
        }
        if (!other.getFromId().isEmpty()) {
          fromId_ = other.fromId_;
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.zzh.protocol.Ack.AckMsg parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.zzh.protocol.Ack.AckMsg) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private long id_ ;
      /**
       * <pre>
       * 消息唯一ID
       * </pre>
       *
       * <code>int64 id = 1;</code>
       */
      public long getId() {
        return id_;
      }
      /**
       * <pre>
       * 消息唯一ID
       * </pre>
       *
       * <code>int64 id = 1;</code>
       */
      public Builder setId(long value) {
        
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 消息唯一ID
       * </pre>
       *
       * <code>int64 id = 1;</code>
       */
      public Builder clearId() {
        
        id_ = 0L;
        onChanged();
        return this;
      }

      private long timeStamp_ ;
      /**
       * <pre>
       *创建时间
       * </pre>
       *
       * <code>int64 timeStamp = 2;</code>
       */
      public long getTimeStamp() {
        return timeStamp_;
      }
      /**
       * <pre>
       *创建时间
       * </pre>
       *
       * <code>int64 timeStamp = 2;</code>
       */
      public Builder setTimeStamp(long value) {
        
        timeStamp_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *创建时间
       * </pre>
       *
       * <code>int64 timeStamp = 2;</code>
       */
      public Builder clearTimeStamp() {
        
        timeStamp_ = 0L;
        onChanged();
        return this;
      }

      private int fromModule_ = 0;
      /**
       * <code>.com.zzh.protocol.AckMsg.Module fromModule = 3;</code>
       */
      public int getFromModuleValue() {
        return fromModule_;
      }
      /**
       * <code>.com.zzh.protocol.AckMsg.Module fromModule = 3;</code>
       */
      public Builder setFromModuleValue(int value) {
        fromModule_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>.com.zzh.protocol.AckMsg.Module fromModule = 3;</code>
       */
      public com.zzh.protocol.Ack.AckMsg.Module getFromModule() {
        @SuppressWarnings("deprecation")
        com.zzh.protocol.Ack.AckMsg.Module result = com.zzh.protocol.Ack.AckMsg.Module.valueOf(fromModule_);
        return result == null ? com.zzh.protocol.Ack.AckMsg.Module.UNRECOGNIZED : result;
      }
      /**
       * <code>.com.zzh.protocol.AckMsg.Module fromModule = 3;</code>
       */
      public Builder setFromModule(com.zzh.protocol.Ack.AckMsg.Module value) {
        if (value == null) {
          throw new NullPointerException();
        }
        
        fromModule_ = value.getNumber();
        onChanged();
        return this;
      }
      /**
       * <code>.com.zzh.protocol.AckMsg.Module fromModule = 3;</code>
       */
      public Builder clearFromModule() {
        
        fromModule_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object destId_ = "";
      /**
       * <pre>
       *此消息接收方的ID
       * </pre>
       *
       * <code>string destId = 4;</code>
       */
      public java.lang.String getDestId() {
        java.lang.Object ref = destId_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          destId_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       *此消息接收方的ID
       * </pre>
       *
       * <code>string destId = 4;</code>
       */
      public com.google.protobuf.ByteString
          getDestIdBytes() {
        java.lang.Object ref = destId_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          destId_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *此消息接收方的ID
       * </pre>
       *
       * <code>string destId = 4;</code>
       */
      public Builder setDestId(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        destId_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *此消息接收方的ID
       * </pre>
       *
       * <code>string destId = 4;</code>
       */
      public Builder clearDestId() {
        
        destId_ = getDefaultInstance().getDestId();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *此消息接收方的ID
       * </pre>
       *
       * <code>string destId = 4;</code>
       */
      public Builder setDestIdBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        destId_ = value;
        onChanged();
        return this;
      }

      private long ackMsgId_ ;
      /**
       * <pre>
       *目标消息ID
       * </pre>
       *
       * <code>int64 ackMsgId = 5;</code>
       */
      public long getAckMsgId() {
        return ackMsgId_;
      }
      /**
       * <pre>
       *目标消息ID
       * </pre>
       *
       * <code>int64 ackMsgId = 5;</code>
       */
      public Builder setAckMsgId(long value) {
        
        ackMsgId_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *目标消息ID
       * </pre>
       *
       * <code>int64 ackMsgId = 5;</code>
       */
      public Builder clearAckMsgId() {
        
        ackMsgId_ = 0L;
        onChanged();
        return this;
      }

      private long ackMsgSessionId_ ;
      /**
       * <code>int64 ackMsgSessionId = 6;</code>
       */
      public long getAckMsgSessionId() {
        return ackMsgSessionId_;
      }
      /**
       * <code>int64 ackMsgSessionId = 6;</code>
       */
      public Builder setAckMsgSessionId(long value) {
        
        ackMsgSessionId_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int64 ackMsgSessionId = 6;</code>
       */
      public Builder clearAckMsgSessionId() {
        
        ackMsgSessionId_ = 0L;
        onChanged();
        return this;
      }

      private java.lang.Object fromId_ = "";
      /**
       * <pre>
       *此消息发送方的id
       * </pre>
       *
       * <code>string fromId = 7;</code>
       */
      public java.lang.String getFromId() {
        java.lang.Object ref = fromId_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          fromId_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <pre>
       *此消息发送方的id
       * </pre>
       *
       * <code>string fromId = 7;</code>
       */
      public com.google.protobuf.ByteString
          getFromIdBytes() {
        java.lang.Object ref = fromId_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          fromId_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *此消息发送方的id
       * </pre>
       *
       * <code>string fromId = 7;</code>
       */
      public Builder setFromId(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        fromId_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *此消息发送方的id
       * </pre>
       *
       * <code>string fromId = 7;</code>
       */
      public Builder clearFromId() {
        
        fromId_ = getDefaultInstance().getFromId();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *此消息发送方的id
       * </pre>
       *
       * <code>string fromId = 7;</code>
       */
      public Builder setFromIdBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        fromId_ = value;
        onChanged();
        return this;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:com.zzh.protocol.AckMsg)
    }

    // @@protoc_insertion_point(class_scope:com.zzh.protocol.AckMsg)
    private static final com.zzh.protocol.Ack.AckMsg DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.zzh.protocol.Ack.AckMsg();
    }

    public static com.zzh.protocol.Ack.AckMsg getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<AckMsg>
        PARSER = new com.google.protobuf.AbstractParser<AckMsg>() {
      @java.lang.Override
      public AckMsg parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new AckMsg(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<AckMsg> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<AckMsg> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public com.zzh.protocol.Ack.AckMsg getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_zzh_protocol_AckMsg_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_zzh_protocol_AckMsg_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\tack.proto\022\020com.zzh.protocol\"\327\001\n\006AckMsg" +
      "\022\n\n\002id\030\001 \001(\003\022\021\n\ttimeStamp\030\002 \001(\003\0223\n\nfromM" +
      "odule\030\003 \001(\0162\037.com.zzh.protocol.AckMsg.Mo" +
      "dule\022\016\n\006destId\030\004 \001(\t\022\020\n\010ackMsgId\030\005 \001(\003\022\027" +
      "\n\017ackMsgSessionId\030\006 \001(\003\022\016\n\006fromId\030\007 \001(\t\"" +
      ".\n\006Module\022\n\n\006CLIENT\020\000\022\n\n\006SERVER\020\001\022\014\n\010TRA" +
      "NSFER\020\002B\005B\003Ackb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_com_zzh_protocol_AckMsg_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_zzh_protocol_AckMsg_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_zzh_protocol_AckMsg_descriptor,
        new java.lang.String[] { "Id", "TimeStamp", "FromModule", "DestId", "AckMsgId", "AckMsgSessionId", "FromId", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
