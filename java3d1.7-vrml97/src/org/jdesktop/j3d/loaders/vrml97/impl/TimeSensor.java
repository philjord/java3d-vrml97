/*
 * $RCSfile: TimeSensor.java,v $
 *
 *      @(#)TimeSensor.java 1.28 98/11/06 16:27:00
 *
 * Copyright (c) 1996-1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 *
 * $Revision: 1.2 $
 * $Date: 2005/02/03 23:07:03 $
 * $State: Exp $
 */
/*
 * @Author: Rick Goldberg
 * @Author: Doug Gehringer
 *
 */
package org.jdesktop.j3d.loaders.vrml97.impl;

/**  Description of the Class */
public class TimeSensor extends Node {

    // exposedField

    SFTime cycleInterval;
    SFBool enabled;
    SFBool loop;
    SFTime startTime;
    SFTime stopTime;

    // eventOut

    SFTime cycleTime;
    SFFloat fraction;
    SFBool isActive;
    SFTime time;

    double last = 0.0;
    boolean simActive = false;

    // when the sensor is active (enabled) the corresponding events
    // are ignored.
    //

    double activeCycleInterval;
    double activeStartTime;
    double activeStopTime;

    /**
     *Constructor for the TimeSensor object
     *
     *@param  loader Description of the Parameter
     */
    public TimeSensor(Loader loader) {
        super(loader);

        cycleInterval = new SFTime(1.0);
        enabled = new SFBool(true);
        loop = new SFBool(false);
        startTime = new SFTime(0.0);
        stopTime = new SFTime(0.0);
        cycleTime = new SFTime(0.0);
        fraction = new SFFloat(0.0f);
        isActive = new SFBool(false);
        time = new SFTime(0.0);

        loader.addTimeSensor(this);
        initFields();
    }

    /**
     *Constructor for the TimeSensor object
     *
     *@param  loader Description of the Parameter
     *@param  cycleInterval Description of the Parameter
     *@param  enabled Description of the Parameter
     *@param  loop Description of the Parameter
     *@param  startTime Description of the Parameter
     *@param  stopTime Description of the Parameter
     */
    public TimeSensor(Loader loader, SFTime cycleInterval, SFBool enabled,
            SFBool loop, SFTime startTime, SFTime stopTime) {

        super(loader);
        this.cycleInterval = cycleInterval;
        this.enabled = enabled;
        this.loop = loop;
        this.startTime = startTime;
        this.stopTime = stopTime;
        cycleTime = new SFTime(0.0);
        fraction = new SFFloat(0.0f);
        isActive = new SFBool(false);
        time = new SFTime(0.0);

        loader.addTimeSensor(this);
        initFields();
    }

    /**  Description of the Method */
    void doneParse() {
        if (enabled.value == true) {
            if ((loop.value == true) && (stopTime.time <= startTime.time)) {
                isActive.setValue(true);
            }
        }
        simActive = true;
        implReady = true;
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("enabled")) {
            //simTick(Time.getNow());
            simTick(time);
        }
        else if (eventInName.equals("startTime")) {
            //System.out.println("TimeSensor("+this+
            //") startTime called time = "+ startTime.time);
            if (isActive.value == true) {
                // ignore by resetting the value back to the previous
                startTime.time = activeStartTime;
            }
            else {
                isActive.setValue(true);
                activeStartTime = startTime.time;
                activeStopTime = stopTime.time;
            }
        }
        else if (eventInName.equals("stopTime")) {
            if (isActive.value == true) {
                // ignore by resetting the value back to the previous
                // check
                if (activeStopTime < activeStartTime) {
                    stopTime.time = activeStopTime;
                }
                if ((activeStartTime < stopTime.time) &&
                        (stopTime.time <= time)) {
                    isActive.setValue(false);
                }
            }
        }
        else if (eventInName.equals("cycleInterval")) {
            if (isActive.value == true) {
                cycleInterval.time = activeCycleInterval;
            }
        }
        else if (
                eventInName.equals("loop") ||
                eventInName.equals("route_enabled") ||
                eventInName.equals("route_startTime") ||
                eventInName.equals("route_stopTime") ||
                eventInName.equals("route_cycleInterval") ||
                eventInName.equals("route_loop")) {
            ;
        }
        else if (eventInName.equals("isActive")) {
            if (isActive.value) {
                activeStartTime = startTime.time;
                activeStopTime = stopTime.time;
                activeCycleInterval = cycleInterval.time;
            }
        }
        else {
            System.err.println("TimeSensor: unknown eventIn " + eventInName);
        }

    }

    float lastF = 0.0f;

    /**
     *  Description of the Method
     *
     *@param  updateTime Description of the Parameter
     */
    void updateFraction(double updateTime) {

        double delta;
        double f;
        float fractionValue = 0.0f;

        activeStopTime = stopTime.time;
        activeCycleInterval = cycleInterval.time;
        activeStartTime = startTime.time;

        delta = updateTime - startTime.time;

        f = Math.IEEEremainder(delta, cycleInterval.time);

        if (f < 0.0) {// IEEEremander values can be < 0
            f += cycleInterval.time;
        }
        if (f <= 0.0) {
            if (updateTime > startTime.time) {
                fractionValue = 1.0f;
            }
            else {
                fractionValue = 0.0f;
            }
        }
        else {
            fractionValue = (float) (f / cycleInterval.time);
        }
        if (fractionValue < lastF) {
            cycleTime.setValue(updateTime);
        }
        lastF = fractionValue;
        fraction.setValue(fractionValue);
    }

    /**
     *  Description of the Method
     *
     *@param  now Description of the Parameter
     */
    void simTick(double now) {

        time.setValue(now);
        //System.out.println("TimeSensor("+this+") enabled"+enabled.value);
        //System.out.println("isActive"+isActive.value);
        //System.out.println("loop"+loop.value);
        //System.out.println("sim tick"+now);
        //System.out.println("startTime"+startTime.time);
        //System.out.println("stopTime"+stopTime.time);
        //System.out.println("cycleInterval"+cycleInterval.time);
        //System.out.println("cycleTime"+cycleTime.time);
        //System.out.println("time"+time.time);
        //System.out.println();

        if (enabled.value == true) {

            if ((now >= stopTime.time) && (stopTime.time > startTime.time) &&
                    (!loop.value)) {
                if (isActive.value) {
                    updateFraction(stopTime.time);
                    isActive.setValue(false);
                }
            }
            else if ((now >= (startTime.time + cycleInterval.time) &&
                    (!loop.value))) {
                if (isActive.value) {
                    updateFraction(startTime.time + cycleInterval.time);
                    isActive.setValue(false);
                }
            }
            else if ((loop.value) && (stopTime.time > startTime.time) &&
                    (now >= stopTime.time)) {
                if (isActive.value) {
                    isActive.setValue(false);
                    updateFraction(stopTime.time);
                }
            }
            else if ((loop.value) && (stopTime.time <= startTime.time) &&
                    (now >= startTime.time)) {
                if (!isActive.value) {
                    isActive.setValue(true);
                }
                updateFraction(now);
            }
            else if ((now > startTime.time) && (!isActive.value)) {
                isActive.setValue(true);
                updateFraction(now);
            }
            else if (isActive.value) {
                updateFraction(now);
            }
        }
        else {// enabled.value == false
            activeStopTime = -1.0;
            activeStartTime = -1.0;
            activeCycleInterval = -1.0;
        }
    }


    /**  Description of the Method */
    void initFields() {
        cycleInterval.init(this, FieldSpec, Field.EXPOSED_FIELD,
                "cycleInterval");
        enabled.init(this, FieldSpec, Field.EXPOSED_FIELD, "enabled");
        loop.init(this, FieldSpec, Field.EXPOSED_FIELD, "loop");
        startTime.init(this, FieldSpec, Field.EXPOSED_FIELD, "startTime");
        stopTime.init(this, FieldSpec, Field.EXPOSED_FIELD, "stopTime");
        cycleTime.init(this, FieldSpec, Field.EVENT_OUT, "cycleTime");
        fraction.init(this, FieldSpec, Field.EVENT_OUT, "fraction");
        isActive.init(this, FieldSpec, Field.EVENT_OUT, "isActive");
        time.init(this, FieldSpec, Field.EVENT_OUT, "time");
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new TimeSensor(loader, (SFTime) cycleInterval.clone(),
                (SFBool) enabled.clone(), (SFBool) loop.clone(),
                (SFTime) startTime.clone(), (SFTime) stopTime.clone());
    }

    /**
     *  Gets the type attribute of the TimeSensor object
     *
     *@return  The type value
     */
    public String getType() {
        return "TimeSensor";
    }

}

