package com.cainiao.funcommonlibrary.okhttp.body;

import com.cainiao.funcommonlibrary.okhttp.callback.OkProgressCallback;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
public class RequestProgressBody extends RequestBody {

    private final RequestBody requestBody;
    private final OkProgressCallback progressCallback;
    private BufferedSink bufferedSink;

    public RequestProgressBody(RequestBody requestBody, OkProgressCallback progressCallback) {
        this.requestBody = requestBody;
        this.progressCallback = progressCallback;
    }

    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long bytesWritten = 0L;
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                if (progressCallback != null && contentLength>0) {
                    progressCallback.onOKProgress(bytesWritten,contentLength);
                }
            }
        };
    }
}
