
package com.bilibili.player_ix.noixmod_api.register.deprecated;

import org.NineAbyss9.code.Code;

import java.util.Objects;

public final class ErrorCode implements Code
{
    final int id;
    final String message;
    ErrorCode(final int id, final String message) {
        this.id = id;
        this.message = message;
    }

    static ErrorCode create(final int id, final String st) {
        return new ErrorCode(id, st);
    }

    public String read()
    {
        return String.valueOf(id);
    }

    public void write(final String s)
    {
        throw new UnsupportedOperationException();
    }

    int getId() {
        return id;
    }

    String getMessage() {
        return message;
    }

    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null || this.getClass() != other.getClass())
            return false;
        ErrorCode errorCode = (ErrorCode)other;
        return this.getId() == errorCode.getId() && Objects.equals(this.getMessage(), errorCode.getMessage());
    }

    public int hashCode() {
        return Objects.hash(this.getId(), this.getMessage());
    }

    public String toString() {
        return "ErrorCode{" +
                "id:" + id +
                ", message:'" + message + '\'' +
                '}';
    }
}
