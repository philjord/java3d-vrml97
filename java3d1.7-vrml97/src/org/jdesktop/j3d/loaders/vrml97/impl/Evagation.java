/*
 * $RCSfile: Evagation.java,v $
 *
 *      @(#)Evagation.java 1.31 99/03/01 14:33:18
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
 * $Date: 2005/02/03 23:06:55 $
 * $State: Exp $
 */
package org.jdesktop.j3d.loaders.vrml97.impl;
import java.applet.Applet;
import java.awt.AWTEvent.*;
import java.awt.AWTEvent;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Point;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.util.Enumeration;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;
import vrml.InvalidVRMLSyntaxException;

/**
 * Evagation \Ev`a*ga*tion\, n  A wandering about, excursion, a roving.
 * (Source: Webster's Revised Unbriged Dictionary (1913))
 *
 * This is the event controller for the VRML runtime.  It dispatches events
 * from Java3D to the VRML Sensors and handles the walk/fly/examine Viewpoint
 * animation.
 */
public class Evagation extends Behavior {

    TransformGroup viewTrans = new TransformGroup();
    Browser browser;
    PickRay pickRay;
    WakeupOnCollisionEntry collisionEnter;
    WakeupOnCollisionExit collisionExit;
    WakeupCriterion[] criterion = new WakeupCriterion[7];
    Matrix3d rot = new Matrix3d();
    Matrix3d lastFreeRot = new Matrix3d();
    Matrix3d tmpDirection = new Matrix3d();
    Matrix3d zComp = new Matrix3d();
    Vector3d newDirection = new Vector3d();
    Transform3D positionTrans = new Transform3D();
    Matrix3d rollRot = new Matrix3d();
    Matrix3d pitchRot = new Matrix3d();
    Matrix3d yawRot = new Matrix3d();
    double rollAngle = 0.0;
    double pitchAngle = 0.0;
    double yawAngle = 0.0;
    double velocity = 0.0;
    double rollAngleDelta = 0.0;
    double pitchAngleDelta = 0.0;
    double yawAngleDelta = 0.0;
    double directionX;// non destroyed direction vector components
    double directionY;
    double directionZ;
    double acceleration;
    final static double SPEED = .005;
    double speed = SPEED;
    double angularSpeed = SPEED / 420.0;
    Transform3D currentView;// Orientation and Translation
    Vector3d currentPosn;// Translation
    Vector3d lastFreePosn = new Vector3d();
    Point mousePosn;
    Point3d eyePosn = new Point3d();
    double eyeZoffset = 0.0;
    double startX = 0.0;
    double startY = 0.0;
    double startZ = 0.0;
    double lastT = 0.0;
    double deltaT = 0.0;
    static double MAXANG = Math.PI * 2.0;
    int last_x, last_y;
    int first_x, first_y;
    int drag_dx, drag_dy;
    int eid;
    boolean initialized;
    // bug: first time causes null ptr exception
    boolean firstTime = true;// for resetWakeups
    boolean collided = false;
    // store the current dragsensor, so mouse
    // motion means something even if the mouse isn't over
    // the sensor until mouse released.
    String navMode;
    SphereSensor sceneExaminer = null;
    DragSensor curDragSensor;
    boolean dragIsSensor = false;

    // pick sphere stuff
    Transform3D tfm = new Transform3D();
    Transform3D tfmO = new Transform3D();
    org.jogamp.java3d.Appearance pickAppear;
    org.jogamp.java3d.TransparencyAttributes pickTransat;
    RGroup pickSphereHandle;
    TransformGroup pickSphereLocator = new TransformGroup();

    // Reusable vars
    Point3d p1 = new Point3d();
    Point3d p2 = new Point3d();

    /**
     *Constructor for the Evagation object
     *
     *@param  initBrowser Description of the Parameter
     */
    public Evagation(Browser initBrowser) {
        browser = initBrowser;
        initialized = false;
        collided = false;
        currentView = new Transform3D();
        currentView.setIdentity();
        pickRay = new PickRay();
    }

    /**  Description of the Method */
    public void initialize() {
        initialize(new BoundingSphere(new Point3d(), 1000.0),
                new Vector3d(startX, startY, startZ));

    }

    /**
     *  Description of the Method
     *
     *@param  bounds Description of the Parameter
     *@param  currentPosn Description of the Parameter
     */
    public void initialize(BoundingSphere bounds, Vector3d currentPosn) {

        Transform3D initTransform = new Transform3D();
        setSchedulingBounds(bounds);
        this.currentPosn = currentPosn;

        currentView.setTranslation(currentPosn);

        rollAngle = 0.0;
        pitchAngle = 0.0;
        rollAngleDelta = 0.0;
        pitchAngleDelta = 0.0;
        yawAngleDelta = 0.0;
        yawAngleDelta = 0.0;
        velocity = 0;

        initialized = true;
        resetWakeups();
    }

    /**
     *  Sets the viewGroup attribute of the Evagation object
     *
     *@param  newViewTrans The new viewGroup value
     */
    public void setViewGroup(TransformGroup newViewTrans) {
        viewTrans = newViewTrans;

        // getBounds only works with Morph and Shape now
        //Bounds b = viewTrans.getBounds();
        Bounds b = new BoundingSphere();
        if (browser.curNavInfo == null) {
            browser.curNavInfo = new NavigationInfo(browser.loader);
        }
        else {
            double sizex = browser.curNavInfo.avatarSize.mfloat[0] / 2.0f;
            double sizey;
            if (browser.curNavInfo.avatarSize.mfloat.length > 1) {
                sizey = browser.curNavInfo.avatarSize.mfloat[1] / 2.0f;
            }
            else {
                sizey = browser.curNavInfo.avatarSize.mfloat[0] / 2.0f;
            }
            double sizez;
            if (browser.curNavInfo.avatarSize.mfloat.length > 2) {
                sizez = browser.curNavInfo.avatarSize.mfloat[2] / 2.0f;
            }
            else {
                sizez = browser.curNavInfo.avatarSize.mfloat[0] / 2.0f;
            }
            b = new BoundingBox(new Point3d(-sizex, -sizey, -sizez),
                    new Point3d(sizex, sizey, sizez));
            navMode = browser.curNavInfo.type.strings[0];
            if (!navMode.equals("EXAMINE") && !navMode.equals("WALK")) {
                navMode = "WALK";
            }
        }//else b = new BoundingSphere();
        speed = browser.curNavInfo.speed.value * SPEED;
        viewTrans.setCollisionBounds(b);
        viewTrans.setBounds(b);
        viewTrans.getTransform(currentView);
        Vector3d vc = new Vector3d();
        currentView.get(vc);
        startX = vc.x;
        startY = vc.y;
        startZ = vc.z;

        if (sceneExaminer != null && navMode.equals("WALK")) {
            try {
                browser.deleteRoute(sceneExaminer, "rotation",
                        browser.curSceneT, "rotation");
                browser.curSceneT.impl.setUserData(null);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            browser.curViewpoint.updateViewTrans();
        }
        if (navMode.equals("EXAMINE")) {
            // bug: it can sometimes happen that in examine mode with
            // no viewpoint specified that the sceneExaminer is null when
            // it actually wasn't expected
            if (browser.sceneExaminer == null) {
                browser.sceneExaminer =
                        new SphereSensor(browser.loader);
                try {
                    browser.sceneExaminer.updateParent(browser.curSceneT.impl);
                }
                catch (NullPointerException npe) {
                    ;
                }
            }
            sceneExaminer = browser.sceneExaminer;
            try {
                browser.addRoute(sceneExaminer, "rotation",
                        browser.curSceneT, "rotation");
            }
            catch (Exception e) {
                e.printStackTrace();
            }// will null first time
        }
        //resetWakeups();
    }

    /**  Description of the Method */
    void resetWakeups() {
        //if ( !firstTime ) {
        SceneGraphPath viewPath =
                new SceneGraphPath(browser.locale, viewTrans);
        //collisionEnter = new WakeupOnCollisionEntry(viewPath);
        //collisionExit = new WakeupOnCollisionExit(viewPath);
        //criterion[0] = collisionEnter;
        //criterion[1] = collisionExit;
        criterion = new WakeupCriterion[7];
        criterion[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_MOVED);
        criterion[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
        criterion[2] = new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED);
        criterion[3] = new WakeupOnAWTEvent(MouseEvent.MOUSE_CLICKED);
        criterion[4] = new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED);
        criterion[5] = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
        criterion[6] = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);

        wakeupOn(new WakeupOr(criterion));
        //} else { firstTime = false; }
        firstTime = false;
    }


    /**
     *  Description of the Method
     *
     *@param  critter Description of the Parameter
     */
    public void processStimulus(Iterator<WakeupCriterion> critter) {
        WakeupCriterion wakeup;
        while (critter.hasNext()) {
            wakeup = (WakeupCriterion) critter.next();
            //if ( wakeup instanceof WakeupOnCollisionEntry ) {
            //collided = true;
            //currentView.setRotationScale( lastFreeRot );
            //currentView.setTranslation( lastFreePosn );
            //resetWakeups();
            //System.out.println("collided");
            //} else if ( wakeup instanceof WakeupOnCollisionExit ) {
            //collided = false;
            //System.out.println("exit");
            //resetWakeups();
            //} else if ( wakeup instanceof WakeupOnAWTEvent ) {
            AWTEvent[] awts = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
            for (int i = 0; i < awts.length; i++) {
                processEvent(awts[i]);
            }
            resetWakeups();
            //}

        }
    }


    /**
     *  Description of the Method
     *
     *@param  evt Description of the Parameter
     */
    void processEvent(AWTEvent evt) {
        if (!initialized) {
            System.out.println("Event before behavior is initialized");
            return;
        }
        if (evt instanceof KeyEvent) {
            processKeyEvent((KeyEvent) evt);
        }
        else if (evt instanceof MouseEvent) {
            processMouseEvent((MouseEvent) evt);
        }
        else if (evt instanceof FocusEvent) {
            processFocusEvent((FocusEvent) evt);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  evt Description of the Parameter
     */
    public void processMouseEvent(MouseEvent evt) {

        eid = evt.getID();
        boolean mmb = ((evt.getModifiers() & MouseEvent.BUTTON2_MASK) > 0);
        switch (eid) {
            case MouseEvent.MOUSE_DRAGGED:
                mousePosn = evt.getPoint();

                drag_dx = mousePosn.x - first_x;
                drag_dy = mousePosn.y - first_y;
                last_x = mousePosn.x;
                last_y = mousePosn.y;

                // this must be here, and really want to do this in vworld coords
                if (drag_dx * drag_dx + drag_dy * drag_dy > 100) {
                    mouseDragged(evt.getWhen(), drag_dx, drag_dy, first_x, first_y, mmb);
                }

                if (dragIsSensor) {
                    velocity = 0;
                }
                else {
                    pitchAngleDelta = -drag_dx * angularSpeed;
                    if (evt.isShiftDown()) {
                        yawAngleDelta = -drag_dy * angularSpeed;
                    }
                    else {
                        velocity = (drag_dy) * speed;
                    }
                }

                break;
            case MouseEvent.MOUSE_CLICKED:
                mousePosn = evt.getPoint();
                first_x = last_x = mousePosn.x;
                first_y = last_y = mousePosn.y;
                mouseClicked(evt.getWhen(), last_x, last_y, mmb);
                break;
            case MouseEvent.MOUSE_PRESSED:
                mousePosn = evt.getPoint();
                first_x = mousePosn.x;
                first_y = mousePosn.y;
                break;
            case MouseEvent.MOUSE_RELEASED:
                clearDragState();
                curDragSensor = null;
                dragIsSensor = false;
                break;
            default:
                break;
        }
        update();

    }

    /**
     *  Description of the Method
     *
     *@param  evt Description of the Parameter
     */
    private void processKeyEvent(KeyEvent evt) {
        int key;
        double ROT_ANGLE = 0.03;
        double VELOCITY = 5.0;
        key = evt.getKeyCode();
        if (evt.getID() == KeyEvent.KEY_RELEASED) {
            velocity = 0.0;
            rollAngleDelta = 0.0;
            pitchAngleDelta = 0.0;
            yawAngleDelta = 0.0;
        }

        // todo: make a switch statement instead
        if (evt.getID() == KeyEvent.KEY_PRESSED) {
            if (key == KeyEvent.VK_UP) {
                velocity = -VELOCITY;
            }
            else if (key == KeyEvent.VK_DOWN) {
                velocity = VELOCITY;
            }
            else if (key == KeyEvent.VK_LEFT) {
                pitchAngleDelta += ROT_ANGLE;
            }
            else if (key == KeyEvent.VK_RIGHT) {
                pitchAngleDelta -= ROT_ANGLE;
            }
            else if (key == KeyEvent.VK_S) {
                resetViewpoint();
            }
            else if (key == KeyEvent.VK_C) {
                pitchAngle = 0.0;
                pitchAngleDelta = 0.0;
                rollAngle = 0.0;
                rollAngleDelta = 0.0;
                velocity = 0.0;
            }
            else if (key == KeyEvent.VK_D) {
                browser.treePrinter.print(browser.curScene);
            }
            else if (key == KeyEvent.VK_SLASH) {
                browser.outputTiming();
            }
            else if (key == KeyEvent.VK_ESCAPE) {
                browser.outputTiming();
                browser.shutDown();
            }
            else if (key == KeyEvent.VK_PAGE_UP) {
                yawAngleDelta = 2.0 * SPEED;
            }
            else if (key == KeyEvent.VK_PAGE_DOWN) {
                yawAngleDelta = -2.0 * SPEED;
            }
        }
        calcTransform();
        viewTrans.setTransform(currentView);

    }

    /**
     *  Description of the Method
     *
     *@param  evt Description of the Parameter
     */
    private void processFocusEvent(FocusEvent evt) {
        if (evt.getID() == FocusEvent.FOCUS_GAINED) {
            //System.out.println("XAutoRepeatOn(display) ... please ");
        }
        else if (evt.getID() == FocusEvent.FOCUS_LOST) {
            //System.out.println("XAutoRepeatOff(display) ... please ");
        }
    }

    /**  Description of the Method */
    void forceUpDown() {
        curDragSensor = null;
        clearDragState();
        first_x = last_x = mousePosn.x;
        first_y = last_y = mousePosn.y;
        mouseDragged(Time.getNow(), drag_dx, drag_dy, first_x, first_y, false);

    }

    /**  Description of the Method */
    void clearDragState() {
        drag_dx = drag_dy = 0;
        first_x = first_y = -1;
        rollAngleDelta = pitchAngleDelta = yawAngleDelta = 0.0;
        velocity = 0.0;
        if (curDragSensor != null) {
            curDragSensor.offset();
        }
    }

    /**  Description of the Method */
    private void calcTransform() {

        //if (!collided) {
        positionTrans.set(velocity * deltaT);
        zComp.setIdentity();
        zComp.setRow(2, 0, 0, 1);

        rot.setIdentity();
        rot.mul(rollRot);
        rot.mul(pitchRot);
        rot.mul(yawRot);

        tmpDirection.mul(rot, zComp);
        newDirection.x = directionX = tmpDirection.m02;
        newDirection.y = directionY = tmpDirection.m12;
        newDirection.z = directionZ = tmpDirection.m22;

        positionTrans.transform(newDirection);
        currentPosn.add(newDirection);

        rollAngle = Math.IEEEremainder(rollAngle + rollAngleDelta, Math.PI * 2.0);
        pitchAngle = Math.IEEEremainder(pitchAngle + pitchAngleDelta,
                Math.PI * 2.0);
        yawAngle = Math.IEEEremainder(yawAngle + yawAngleDelta, Math.PI * 2.0);

        rollRot.rotZ(rollAngle);
        pitchRot.rotY(pitchAngle);
        yawRot.rotX(yawAngle);

        currentView.setRotationScale(rot);
        currentView.setTranslation(currentPosn);
        //} else {
        //currentView.setRotationScale( lastFreeRot );
        //currentView.setTranslation( lastFreePosn );
        //}
    }

    /**
     *  Description of the Method
     *
     *@param  now Description of the Parameter
     */
    public void simTick(double now) {
        // it is not specified when the event acutally happens, so
        // that is why collided gets checked all the time
        if (!collided) {
            deltaT = now - lastT;
            lastFreeRot.set(rot);
            lastFreePosn.set(currentPosn);
        }
        else {
            if (browser.debug) {
                System.out.println("collided");
            }
            deltaT = 0.0;// this effectively freezes the viewplatfrom
            // upon collision. it is not entirely the desired effect
            //deltaT = now-lastT;
        }
        lastT = now;
        update();
        // This is an experimental drag-n-hold feature. Uncomment to enable
        // the SphereSensor SFBool autoSpin from within a file
        //if ( curDragSensor != null )
        //curDragSensor.simTick(now);
    }

    /**  Description of the Method */
    void update() {
        if (initialized) {
            calcTransform();
            // any new collision info should happen after this call
            if ((currentView.getType() & Transform3D.CONGRUENT) != 0) {
                viewTrans.setTransform(currentView);
            }
        }
    }

    /**  Description of the Method */
    public void resetViewpoint() {
        rollAngle = 0.0;
        pitchAngle = 0.0;
        yawAngle = 0.0;
        rollAngleDelta = 0.0;
        pitchAngleDelta = 0.0;
        yawAngleDelta = 0.0;
        velocity = 0;
        currentPosn = new Vector3d(startX, startY, startZ);
    }

    /**
     *  Gets the pickRay attribute of the Evagation object
     *
     *@param  xpos Description of the Parameter
     *@param  ypos Description of the Parameter
     *@return  The pickRay value
     */
    private PickRay getPickRay(int xpos, int ypos) {
        Transform3D motion = new Transform3D();
        Point3d mousePosn = new Point3d();
        Vector3d mouseVec = new Vector3d();

        browser.canvas.getCenterEyeInImagePlate(eyePosn);
        browser.canvas.getPixelLocationInImagePlate(xpos, ypos, mousePosn);
        browser.canvas.getImagePlateToVworld(motion);

        //if(navMode.equals("EXAMINE")) eyeZoffset = eyePosn.z;
        //else eyeZoffset=0.0;
        eyeZoffset = eyePosn.z;

        motion.transform(eyePosn);
        motion.transform(mousePosn);
        mouseVec.sub(mousePosn, eyePosn);
        mouseVec.normalize();

        pickRay.set(eyePosn, mouseVec);

        return pickRay;
    }

    /**
     *  Description of the Method
     *
     *@param  step Description of the Parameter
     *@param  path Description of the Parameter
     */
    private void pickEcho(org.jogamp.java3d.Node step, SceneGraphPath path) {
        try {
            org.jogamp.java3d.Bounds b = step.getBounds();
            if (b instanceof org.jogamp.java3d.BoundingSphere) {
                Point3d p = new Point3d();
                ((BoundingSphere) b).getCenter(p);
                try {
                    step.getLocalToVworld(this.tfm);
                }
                catch (IllegalSharingException e) {
                    try {
                        step.getLocalToVworld(path, this.tfm);
                    }
                    catch (Exception f) {
                        /*f.printStackTrace();*/
                        ;
                    }
                }
                this.tfmO.set(new Vector3d(p));
                this.tfm.mul(this.tfmO);
                org.jogamp.java3d.utils.geometry.Sphere sp =
                        new org.jogamp.java3d.utils.geometry.Sphere(
                        (float) ((BoundingSphere) b).getRadius());
                attachPickSphere(sp);
            }
            else {
                if (browser.debug) {
                    System.out.println("node bounds are not " +
                            "BoundingSphere");
                }
            }
            // node might not have capability to read bounds
        }
        catch (Exception ed) {
            System.out.println("Exception trying to echo pick");
            ed.printStackTrace();
        }
    }


    /**
     *  Description of the Method
     *
     *@param  when Description of the Parameter
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     *@param  mmb Description of the Parameter
     */
    void mouseClicked(double when, int x, int y, boolean mmb) {

        if (browser.debug) {
            System.out.println("Evagation.mouseClicked() called");
        }
        // all touch events should have the same timestamp.  This will
        // prevent multiple events on the same sensor from the same pick
        double touchTime = Time.getNow();
        browser.beginRoute(touchTime);

        SceneGraphPath[] stuffPicked =
                browser.curScene.pickAllSorted(getPickRay(x, y));
        if (stuffPicked != null) {
            if (browser.debug) {
                System.out.println("stuffPicked.length=" + stuffPicked.length);
            }
            for (int i = 0; i < stuffPicked.length; i++) {
                if (browser.debug) {
                    System.out.println("stuffPicked[" + i + "] is " +
                            stuffPicked[i]);
                }
                VrmlSensor vs = (VrmlSensor) null;
                Vector sv = (Vector) null;
                org.jogamp.java3d.Node step = null;
                if (browser.debug) {
                    System.out.println("stuffPicked[" + i + "].nodeCount() = " +
                            stuffPicked[i].nodeCount());
                }
                for (int j = 0; j < stuffPicked[i].nodeCount(); j++) {
                    step = stuffPicked[i].getNode(j);
                    if (browser.debug || browser.pickEcho) {
                        pickEcho(step, stuffPicked[i]);
                    }
                    Object o = step.getUserData();
                    if (browser.debug && j == 0) {
                        System.out.println(i + " " + j + "Picked node is " +
                                step + " user data is " + o);
                        System.out.println(stuffPicked[i].getObject());
                    }
                    if (o instanceof Vector && !((Vector) o).isEmpty()) {
                        // save the bottom most to preserve nesting
                        // note, the userData should be a vector
                        // of sensors for this group.
                        sv = (Vector) o;
                        // rare case
                    }
                    else if (o instanceof SphereSensor) {
                        sv = new Vector();
                        sv.addElement(o);
                        step.setUserData(sv);
                    }
                }

                if (sv != null) {
                    Enumeration e = sv.elements();
                    while (e.hasMoreElements()) {
                        vs = (VrmlSensor) (e.nextElement());
                        if (vs instanceof TouchSensor) {
                            TouchSensor ts = (TouchSensor) vs;
                            if (ts.enabled.value == true) {
                                if (browser.debug) {
                                    System.out.println("Calling setValue on " +
                                            "touchTime for sensor " +
                                            ts.toStringId());
                                }
                                ts.touchTime.setValue(touchTime);
                            }
                        }
                        else if (vs instanceof DragSensor && !mmb) {
                            // p1 and p2 are same point, its a click
                            //browser.canvas.getPixelLocationInImagePlate(x,y,p1);
                            //browser.canvas.getPixelLocationInImagePlate(x,y,p2);
                            //((DragSensor)vs).update(p1,p2,step,stuffPicked[i]);
                            //dragIsSensor = true;
                            //curDragSensor = (DragSensor)vs;
                        }
                        else if (vs instanceof Anchor) {
                            String toGoto = ((Anchor) vs).url.strings[0];
                            try {
                                browser.canvas.setCursor(
                                        new Cursor(Cursor.WAIT_CURSOR));
                                browser.loadURL(((Anchor) vs).url.strings,
                                        (String[]) null);
                            }
                            catch (java.io.IOException ioe) {
                                System.err.println("IO exception reading URL");
                            }
                            catch (InvalidVRMLSyntaxException ivse) {
                                System.err.println("VRML parse error");
                            }
                            String status = browser.getName() + " : " +
                                    browser.getDescription();
                            if (browser.container instanceof Applet) {
                                if (toGoto.endsWith(".wrl")) {
                                    ((Applet) (browser.container)).showStatus(
                                            status);
                                }
                                else {
                                    try {
                                        ((Applet) (browser.container)).
                                                getAppletContext().showDocument(
                                                new URL(toGoto));
                                    }
                                    catch (MalformedURLException me) {
                                        ((Applet) (browser.container)).
                                                showStatus(me.toString());
                                    }
                                }
                            }
                            else if (browser.container instanceof Frame) {
                                ((Frame) (browser.container)).setTitle(status);
                                ((org.jdesktop.j3d.loaders.vrml97.Player)
                                        (browser.container)).setGotoString(toGoto);
                            }
                            browser.canvas.setCursor(new
                                    Cursor(Cursor.HAND_CURSOR));
                        }
                    }
                }
            }
        }

        browser.endRoute();
    }

    /**
     *  Description of the Method
     *
     *@param  when Description of the Parameter
     *@param  dx Description of the Parameter
     *@param  dy Description of the Parameter
     *@param  x Description of the Parameter
     *@param  y Description of the Parameter
     *@param  mmb Description of the Parameter
     */
    void mouseDragged(double when, int dx, int dy, int x, int y, boolean mmb) {
        // bug: if a click sensor and drag sensor are in same group,
        // drag never autoRotates.
        browser.beginRoute(Time.getNow());
        if (curDragSensor != null && !mmb) {
            // dragsensors node reference must have already been set
            browser.canvas.getPixelLocationInImagePlate(x, y, p1);
            browser.canvas.getPixelLocationInImagePlate(x + dx, y + dy, p2);
            p1.z = p2.z = eyeZoffset;
            curDragSensor.update(p1, p2, null, null);
        }
        else {
            SceneGraphPath[] stuffDragged =
                    browser.curScene.pickAllSorted(getPickRay(x, y));
            if (stuffDragged != null) {
                for (int i = 0; i < stuffDragged.length; i++) {
                    VrmlSensor vs = (VrmlSensor) null;
                    Vector sv = (Vector) null;
                    org.jogamp.java3d.Node step = null;
                    for (int j = 0; j < stuffDragged[i].nodeCount(); j++) {
                        try {
                            step = stuffDragged[i].getNode(j);
                            Object o = step.getUserData();
                            if (browser.debug && j == 0) {
                                System.out.println(i + "." + j + ": Picked node is " +
                                        step + " user data is " + o);
                            }

                            if (o instanceof Vector && !((Vector) o).isEmpty()) {
                                // save the bottom most to preserve nesting
                                // note, the userData should be a vector
                                // of sensors for this group.
                                sv = (Vector) o;
                                // the scneExaminer should be the exception
                                if (navMode.equals("EXAMINE")) {
                                    j = stuffDragged[i].nodeCount();
                                }// break;
                            }
                            // node might not have capability to read bounds
                        }
                        catch (Exception ed) {
                            ed.printStackTrace();
                        }
                    }
                    if (sv != null) {
                        Enumeration e = sv.elements();
                        while (e.hasMoreElements()) {
                            vs = (VrmlSensor) (e.nextElement());
                            if (vs instanceof DragSensor && !mmb) {
                                browser.canvas.getPixelLocationInImagePlate(x, y, p1);
                                browser.canvas.getPixelLocationInImagePlate(x + dx, y + dy, p2);
                                p1.z = p2.z = eyeZoffset;
                                // this is the first drag segment,
                                // The sensor may need to inspect the stuffDragged path
                                // to get the transform above.
                                ((DragSensor) vs).update(p1, p2, step, stuffDragged[i]);
                                dragIsSensor = true;
                                curDragSensor = (DragSensor) vs;
                            }
                            else {
                                curDragSensor = null;
                            }
                        }
                    }
                }
            }
        }
        browser.endRoute();
    }

    /**
     *  Description of the Method
     *
     *@param  sp Description of the Parameter
     */
    void attachPickSphere(org.jogamp.java3d.utils.geometry.Sphere sp) {
        if (!pickSphereLocator.isLive()) {
            try {
                pickSphereLocator.removeChild(0);
            }
            catch (ArrayIndexOutOfBoundsException aioobe) {
                ;
            }

            pickSphereHandle = new RGroup();
            pickSphereLocator = new TransformGroup();
            pickSphereHandle.addChild(pickSphereLocator);
            pickTransat = new org.jogamp.java3d.TransparencyAttributes(
                    TransparencyAttributes.FASTEST, 0.5f);
            pickTransat.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
            pickTransat.setCapability(TransparencyAttributes.ALLOW_VALUE_READ);
            pickAppear = new org.jogamp.java3d.Appearance();
            pickAppear.setTransparencyAttributes(pickTransat);
            // sphere should have been already created the right size
            sp.setAppearance(pickAppear);
            pickSphereLocator.addChild(sp);
            pickSphereLocator.addChild(new
                    PickSphereTimer(browser, pickSphereHandle, pickTransat));
            pickSphereLocator.setTransform(this.tfm);
            browser.locale.addBranchGraph(pickSphereHandle);
        }
    }

}

