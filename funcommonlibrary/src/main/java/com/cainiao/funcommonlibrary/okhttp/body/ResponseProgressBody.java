package com.cainiao.funcommonlibrary.okhttp.body;

import com.cainiao.funcommonlibrary.okhttp.callback.OkProgressCallback;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ResponseProgressBody extends ResponseBody {

    private final ResponseBody mResponseBody;
    private final OkProgressCallback progressCallback;
    private BufferedSource bufferedSource;

    public ResponseProgressBody(ResponseBody responseBody, OkProgressCallback progressCallback) {
        this.mResponseBody = responseBody;
        this.progressCallback = progressCallback;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {

            long totalBytesRead;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += ((bytesRead != -1) ? bytesRead : 0);
                if (progressCallback != null) {
                    progressCallback.onOKProgress(totalBytesRead,mResponseBody.contentLength());
                }
                return bytesRead;
            }
        };
    }
}
