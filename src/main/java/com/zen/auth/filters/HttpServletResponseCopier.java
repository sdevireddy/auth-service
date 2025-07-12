package com.zen.auth.filters;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class HttpServletResponseCopier extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream copy = new ByteArrayOutputStream();
    private ServletOutputStream outputStream;
    private PrintWriter writer;

    public HttpServletResponseCopier(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("Writer already obtained");
        }

        if (outputStream == null) {
            outputStream = new ServletOutputStream() {
                private final ServletOutputStream original = HttpServletResponseCopier.super.getOutputStream();

                @Override
                public boolean isReady() {
                    return original.isReady();
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                    original.setWriteListener(writeListener);
                }

                @Override
                public void write(int b) throws IOException {
                    original.write(b);
                    copy.write(b);
                }
            };
        }
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) {
            throw new IllegalStateException("OutputStream already obtained");
        }

        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(copy, getCharacterEncoding()), true);
        }

        return writer;
    }

    public byte[] getCopy() {
        return copy.toByteArray();
    }
}

