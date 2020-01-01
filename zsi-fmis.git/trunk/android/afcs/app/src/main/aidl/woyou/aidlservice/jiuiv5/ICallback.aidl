// ICallback.aidl
package woyou.aidlservice.jiuiv5;

// Declare any non-default types here with import statements

interface ICallback {

    oneway void onRunResult(boolean isSuccess);

    oneway void onReturnString(String result);

    oneway void  onRaiseException(int code, String msg);
}
