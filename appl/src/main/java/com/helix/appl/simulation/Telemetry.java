package com.helix.appl.simulation;

import com.helix.lib.ftccomponentinterfaces.TelemetryInterface;

import java.util.Locale;

public class Telemetry implements TelemetryInterface {

    //----------------------------------------------------------------------------------------------
    // Core usage
    //----------------------------------------------------------------------------------------------

    /**
     * Adds an item to the end of the telemetry being built for driver station display. The value shown
     * will be the result of calling {@link String#format(Locale, String, Object...) String.format()}
     * with the indicated format and arguments. The caption and value are shown on the driver station
     * separated by the {@link #getCaptionValueSeparator() caption value separator}. The item
     * is removed if {@link #clear()} or {@link #clearAll()} is called.
     *
     * @param caption   the caption to use
     * @param format    the string by which the arguments are to be formatted
     * @param args      the arguments to format
     * @return          an {@link TelemetryInterface.Item} that can be used to update the value or append further {@link TelemetryInterface.Item}s
     *
     * @see #addData(String, Object)
     * @see #addData(String, Func)
     */
    public TelemetryInterface.Item addData(String caption, String format, Object... args) {return null; }

    /**
     * Adds an item to the end if the telemetry being built for driver station display. The value shown
     * will be the result of calling {@link Object#toString() toString()} on the provided value
     * object. The caption and value are shown on the driver station separated by the {@link
     * #getCaptionValueSeparator() caption value separator}. The item is removed if {@link #clear()}
     * or {@link #clearAll()} is called.
     *
     * @param caption   the caption to use
     * @param value     the value to display
     * @return          an {@link TelemetryInterface.Item} that can be used to update the value or append further {@link TelemetryInterface.Item}s
     *
     * @see #addData(String, String, Object...)
     * @see #addData(String, Func)
     */
    public TelemetryInterface.Item addData(String caption, Object value) {return null;}

    /**
     * Adds an item to the end of the telemetry being built for driver station display. The value shown
     * will be the result of calling {@link Object#toString() toString()} on the object which is
     * returned from invoking valueProducer.{@link Func#value()} value()}. The caption and value are
     * shown on the driver station separated by the {@link #getCaptionValueSeparator() caption value
     * separator}. The item is removed if {@link #clearAll()} is called, but <em>not</em> if
     * {@link #clear()} is called.
     *
     * <p>The valueProducer is evaluated only if actual transmission to the driver station
     * is to occur. This is important, as it provides a means of displaying telemetry which
     * is relatively expensive to evaluate while avoiding computation or delay on evaluations
     * which won't be transmitted due to transmission interval throttling.</p>
     *
     * @param caption           the caption to use
     * @param valueProducer     the object which will provide the value to display
     * @return                  an {@link TelemetryInterface.Item} that can be used to update the value or append further {@link TelemetryInterface.Item}s
     *
     * @see #addData(String, String, Object...)
     * @see #addData(String, Object)
     * @see #addData(String, String, Func)
     * @see #getMsTransmissionInterval()
     */
    // ************** <T> Item addData(String caption, Func<T> valueProducer);

    /**
     * Adds an item to the end of the telemetry being built for driver station display. The value shown
     * will be the result of calling {@link String#format} on the object which is returned from invoking
     * valueProducer.{@link Func#value()} value()}. The caption and value are shown on the driver station
     * separated by the {@link #getCaptionValueSeparator() caption value separator}. The item is removed
     * if {@link #clearAll()} is called, but <em>not</em> if {@link #clear()} is called.
     *
     * <p>The valueProducer is evaluated only if actual transmission to the driver station
     * is to occur. This is important, as it provides a means of displaying telemetry which
     * is relatively expensive to evaluate while avoiding computation or delay on evaluations
     * which won't be transmitted due to transmission interval throttling.</p>
     *
     * @param caption           the caption to use
     * @param valueProducer     the object which will provide the value to display
     * @return                  an {@link TelemetryInterface.Item} that can be used to update the value or append further {@link TelemetryInterface.Item}s
     *
     * @see #addData(String, String, Object...)
     * @see #addData(String, Object)
     * @see #addData(String, Func)
     * @see #getMsTransmissionInterval()
     */
    // ******* <T> Item addData(String caption, String format, Func<T> valueProducer);

    /**
     * Removes an item from the receiver telemetry, if present.
     * @param item  the item to remove
     * @return      true if any change was made to the receive (ie: the item was present); false otherwise
     */
    public boolean removeItem(TelemetryInterface.Item item) {return true;}

    /**
     * Removes all items from the receiver whose value is not to be retained.
     * @see TelemetryInterface.Item#setRetained(Boolean)
     * @see TelemetryInterface.Item#isRetained()
     * @see #clearAll()
     * @see #addData(String, Func)
     */
    public void clear() {}

    /**
     * Removes <em>all</em> items, lines, and actions from the receiver
     *
     * @see #clear()
     */
    public void clearAll() {}

    /**
     * In addition to items and lines, a telemetry may also contain a list of actions.
     * When the telemetry is to be updated, these actions are evaluated before the telemetry
     * lines are composed just prior to transmission. A typical use of such actions is to
     * initialize some state variable, parts of which are subsequently displayed in items.
     * This can help avoid needless re-evaluation.
     *
     * <p>Actions are cleared with {@link #clearAll()}, and can be removed with {@link
     * #removeAction(Object) removeAction()}.</p>
     *
     * @param action    the action to execute before composing the lines telemetry
     * @return          a token by which the action can be later removed.
     * @see #addData(String, Object)
     * @see #removeAction(Object)
     * @see #addLine()
     * @see #update()
     */
    public Object addAction(Runnable action) {return null;}

    /**
     * Removes a previously added action from the receiver.
     * @param token the token previously returned from {@link #addAction(Runnable) addAction()}.
     * @return whether any change was made to the receiver
     */
    public boolean removeAction(Object token) {return true;}

    //----------------------------------------------------------------------------------------------
    // Transmission
    //----------------------------------------------------------------------------------------------

    /**
     * Sends the receiver {@link Telemetry} to the driver station if more than the {@link #getMsTransmissionInterval()
     * transmission interval} has elapsed since the last transmission, or schedules the transmission
     * of the receiver should no subsequent {@link Telemetry} state be scheduled for transmission before
     * the {@link #getMsTransmissionInterval() transmission interval} expires.
     * @return whether a transmission to the driver station occurred or not
     */
    public boolean update() {return true;}

    //----------------------------------------------------------------------------------------------
    // Data Lines
    //----------------------------------------------------------------------------------------------

    /**
     * Instances of {@link TelemetryInterface.Line} build lines of data on the driver station telemetry display.
     */
    interface Line
    {
        /**
         * Adds a new data item at the end of the line which is the receiver.
         * @see Telemetry#addData(String, String, Object...)
         */
        TelemetryInterface.Item addData(String caption, String format, Object... args);
        /**
         * Adds a new data item at the end of the line which is the receiver.
         * @see Telemetry#addData(String, Object)
         */
        TelemetryInterface.Item addData(String caption, Object value);
        /**
         * Adds a new data item at the end of the line which is the receiver.
         * @see Telemetry#addData(String, Func)
         */
        // *********** <T> Item addData(String caption, Func<T> valueProducer);
        /**
         * Adds a new data item at the end of the line which is the receiver.
         * @see Telemetry#addData(String, String, Func)
         */
        // ********** <T> Item addData(String caption, String format, Func<T> valueProducer);
    }

    /**
     * Creates and returns a new line in the receiver {@link Telemetry}.
     * @return a new line in the receiver {@link Telemetry}
     */
    public TelemetryInterface.Line addLine() {return null;}

    /**
     * Creates and returns a new line in the receiver {@link Telemetry}.
     * @param lineCaption the caption for the line
     * @return a new line in the receiver {@link Telemetry}
     */
    public TelemetryInterface.Line addLine(String lineCaption) {return null;}

    /**
     * Removes a line from the receiver telemetry, if present.
     * @param line the line to be removed
     * @return whether any change was made to the receiver
     */
    public boolean removeLine(TelemetryInterface.Line line) {return true;}

    //----------------------------------------------------------------------------------------------
    // Data Items
    //----------------------------------------------------------------------------------------------

    /**
     * Instances of {@link TelemetryInterface.Item} represent an item of data on the drive station telemetry display.
     *
     * @see {@link #addData(String, Object)}
     */
    interface Item
    {
        /**
         * Returns the caption associated with this item.
         * @return the caption associated with this item.
         * @see #setCaption(String)
         * @see #addData(String, Object)
         */
        String getCaption();

        /**
         * Sets the caption associated with this item.
         * @param caption the new caption associated with this item.
         * @return the receiver
         * @see #getCaption()
         */
        TelemetryInterface.Item setCaption(String caption);

        /**
         * Updates the value of this item to be the result of the indicated string formatting operation.
         * @param format    the format of the data
         * @param args      the arguments associated with the format
         * @return the receiver
         * @see #addData(String, String, Object...)
         */
        TelemetryInterface.Item setValue(String format, Object...args);

        /**
         * Updates the value of this item to be the result of applying {@link Object#toString()}
         * to the indicated object.
         * @param value the object to which {@link Object#toString()} should be applied
         * @return the receiver
         * @see #addData(String, Object)
         */
        TelemetryInterface.Item setValue(Object value);

        /**
         * Updates the value of this item to be the indicated value producer.
         * @param valueProducer an object that produces values to be rendered.
         * @return the receiver
         * @see #addData(String, Func)
         */
        // ********* <T> Item setValue(Func<T> valueProducer);

        /**
         * Updates the value of this item to be the indicated value producer.
         * @param format        this string used to format values produced
         * @param valueProducer an object that produces values to be rendered.
         * @return the receiver
         * @see #addData(String, String, Func)
         */
        /// ************ <T> Item setValue(String format, Func<T> valueProducer);

        /**
         * Sets whether the item is to be retained in clear() operation or not.
         * This is initially true for items that whose value is computed with a
         * value producer; otherwise, it is initially false.
         * @param retained if true, then the value will be retained during a clear(). Null will
         *                 return the setting to its initial value.
         * @return the receiver
         * @see #clear()
         * @see #isRetained()
         */
        // Item setRetained(@Nullable Boolean retained);

        /**
         * Returns whether the item is to be retained in a clear() operation.
         * @return whether the item is to be retained in a clear() operation.
         * @see #setRetained(Boolean)
         */
        boolean isRetained();

        /**
         * Adds a new data item in the associated {@link Telemetry} immediately following the receiver.
         * @see #addData(String, String, Object...)
         */
        TelemetryInterface.Item addData(String caption, String format, Object... args);

        /**
         * Adds a new data item in the associated {@link Telemetry} immediately following the receiver.
         * @see #addData(String, Object)
         */
        TelemetryInterface.Item addData(String caption, Object value);

        /**
         * Adds a new data item in the associated {@link Telemetry} immediately following the receiver.
         * @see #addData(String, Func)
         */
        // ******* <T> Item addData(String caption, Func<T> valueProducer);

        /**
         * Adds a new data item in the associated {@link Telemetry} immediately following the receiver.
         * @see #addData(String, String, Func)
         */
        // ******** <T> Item addData(String caption, String format, Func<T> valueProducer);
    }

    //----------------------------------------------------------------------------------------------
    // Properties
    //----------------------------------------------------------------------------------------------

    /**
     * Answers whether {@link #clear()} is automatically called after each call to {@link #update()}.
     * @return whether {@link #clear()} is automatically called after each call to {@link #update()}.
     * @see #setAutoClear(boolean)
     */
    public boolean isAutoClear() {return true;}

    /**
     * Sets whether {@link #clear()} is automatically called after each call to {@link #update()}.
     * @param autoClear if true, {@link #clear()} is automatically called after each call to {@link #update()}.
     */
    public void setAutoClear(boolean autoClear) {}

    /**
     * Returns the minimum interval between {@link Telemetry} transmissions from the robot controller
     * to the driver station
     * @return the minimum interval between {@link Telemetry} transmissions from the robot controller to the diver station
     * @see #setMsTransmissionInterval(int)
     */
    public int getMsTransmissionInterval() {return 1;}

    /**
     * Sets the minimum interval between {@link Telemetry} transmissions from the robot controller
     * to the driver station.
     * @param msTransmissionInterval  the minimum interval between {@link Telemetry} transmissions
     *                                from the robot controller to the driver station
     * @see #getMsTransmissionInterval()
     */
    public void setMsTransmissionInterval(int msTransmissionInterval) {}

    /**
     * Returns the string which is used to separate {@link TelemetryInterface.Item}s contained within a line. The default
     * separator is " | ".
     * @return the string which is use to separate {@link TelemetryInterface.Item}s contained within a line.
     * @see #setItemSeparator(String)
     * @see #addLine()
     */
    public String getItemSeparator() {return "";}

    /**
     * @see #setItemSeparator(String)
     */
    public void setItemSeparator(String itemSeparator) {}

    /**
     * Returns the string which is used to separate caption from value within a {@link Telemetry}
     * {@link TelemetryInterface.Item}. The default separator is " : ";
     * @return the string which is used to separate caption from value within a {@link Telemetry} {@link TelemetryInterface.Item}.
     */
    public String getCaptionValueSeparator() {return "";}

    /**
     * @see #getCaptionValueSeparator()
     */
    public void setCaptionValueSeparator(String captionValueSeparator) {}

    //----------------------------------------------------------------------------------------------
    // Properties
    //----------------------------------------------------------------------------------------------

    /**
     * The {@link TelemetryInterface.Log} in a {@link Telemetry} instance provides an append-only list of messages
     * that appear on the driver station below the {@link TelemetryInterface.Item}s of the {@link Telemetry}.
     * @see #log()
     * @see #addData(String, Object)
     */
    interface Log
    {
        /**
         * {@link TelemetryInterface.Log.DisplayOrder} instances indicate the desired ordering of a {@link #log()}.
         */
        enum DisplayOrder { NEWEST_FIRST, OLDEST_FIRST }

        /**
         * Returns the maximum number of lines which will be retained in a {@link #log()()} and
         * shown on the driver station display.
         * @return the maximum number of lines which will be retained in a {@link #log()()}
         * @see #setCapacity(int)
         */
        int getCapacity();

        /**
         * @see #getCapacity()
         */
        void setCapacity(int capacity);

        /**
         * Returns the order in which data in log is to be displayed on the driver station.
         * @return the order in which data in log is to be displayed on the driver station.
         * @see #setDisplayOrder(TelemetryInterface.Log.DisplayOrder)
         */
        TelemetryInterface.Log.DisplayOrder getDisplayOrder();

        /**
         * @see #getDisplayOrder()
         */
        void setDisplayOrder(TelemetryInterface.Log.DisplayOrder displayOrder);

        /**
         * Adds a new entry the the log. Transmits the updated log to the driver station at the
         * earliest opportunity.
         * @param entry     the new log entry to add
         */
        void add(String entry);

        /**
         * Adds a new entry to the log. Transmits the updated log to the driver station at the
         * earliest opportunity.
         * @param format    the format string used to format the log entry
         * @param args      the data used to format the log entry
         */
        void add(String format, Object...args);

        /**
         * Removes all entries from this {@link TelemetryInterface.Log}
         */
        void clear();
    }

    /**
     * Returns the log of this {@link Telemetry} to which log entries may be appended.
     * @return the log of this {@link Telemetry} to which log entries may be appended.
     * @see TelemetryInterface.Log#addData(String, Object)
     */
    public TelemetryInterface.Log log() {return null;}
}

