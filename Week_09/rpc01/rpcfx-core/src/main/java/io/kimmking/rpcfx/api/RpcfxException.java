package io.kimmking.rpcfx.api;

/**
 * @author yansq
 * @date 2020/12/16
 */
public class RpcfxException extends RuntimeException {
    public static final int DEFAULT_RPC_CODE = 900;
    private int errorCode;
    private String errorMsg;
    public RpcfxException(String errorMsg) {
        this(DEFAULT_RPC_CODE, errorMsg);
    }

    public RpcfxException(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    public RpcfxException(int errorCode, String errorMsg, Throwable t) {
        super(errorMsg, t);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public static RpcfxException of(String errorMsg) {
        return new RpcfxException(errorMsg);
    }
    public static RpcfxException of(Throwable t) {
        return new RpcfxException(DEFAULT_RPC_CODE, "invoke error", t);
    }

    public static RpcfxException of(int errorCode, String errorMsg) {
        return new RpcfxException(errorCode, errorMsg);
    }
    public static RpcfxException of(int errorCode, String errorMsg, Throwable t) {
        return new RpcfxException(errorCode, errorMsg, t);
    }

    @Override
    public String toString() {
        return "[code: " + this.errorCode + ", msg: " + this.errorMsg + "]" + super.toString();
    }
}
