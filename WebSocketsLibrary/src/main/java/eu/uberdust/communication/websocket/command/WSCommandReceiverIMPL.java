package eu.uberdust.communication.websocket.command;


import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.WebSocket;

import java.util.Arrays;


/**
 * Implementation of InsertReading WebSocket.
 */
public final class WSCommandReceiverIMPL implements WebSocket.OnBinaryMessage, WebSocket.OnTextMessage {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(WSCommandReceiverIMPL.class);


    /**
     * On Binary Message arrival.
     *
     * @param data   , data byte array.
     * @param offset , offset.
     * @param length , length/
     */
    @Override
    public void onMessage(final byte[] data, final int offset, final int length) {
        LOGGER.info("Binary message arrived : data (" + Arrays.toString(data) + ")");
        LOGGER.info("Binary message arrived : data.offset (" + offset + ")");
        LOGGER.info("Binary message arrived : data.length (" + length + ")");
        WSCommandReceiverClient.getInstance().update(data, offset, length);
    }

    /**
     * On open connection.
     *
     * @param connection connection instance.
     */
    @Override
    public void onOpen(final Connection connection) {
        LOGGER.info("Connection opened");
    }

    /**
     * On close connection.
     *
     * @param closeCode , close code.
     * @param message   , on string message.
     */
    @Override
    public void onClose(final int closeCode, final String message) {
        LOGGER.info("Connection closed");
        WSCommandReceiverClient.getInstance().disconnect();
        WSCommandReceiverClient.getInstance().restPing();
        try {
            LOGGER.info("reconnecting in 5000");
            Thread.sleep(5000);
            WSCommandReceiverClient.getInstance().connect();
        } catch (Exception e) {
            LOGGER.fatal(e);
        }
    }

    /**
     * On Text Message.
     *
     * @param data , data.
     */
    @Override
    public void onMessage(final String data) {
        LOGGER.info("onMessage "+data);
        WSCommandReceiverClient.getInstance().update(data);
    }
}
