package Method.Client.utils.system;

import com.google.common.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.ASMEventHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Nan0EventRegister {
   public static void register(EventBus bus, Object target) {
      ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners = (ConcurrentHashMap)ReflectionHelper.getPrivateValue(EventBus.class, bus, new String[]{"listeners"});
      Map<Object, ModContainer> listenerOwners = (Map)ReflectionHelper.getPrivateValue(EventBus.class, bus, new String[]{"listenerOwners"});
      if (!listeners.containsKey(target)) {
         ModContainer owner = Loader.instance().getMinecraftModContainer();
         listenerOwners.put(target, owner);
         ReflectionHelper.setPrivateValue(EventBus.class, bus, listenerOwners, new String[]{"listenerOwners"});
         Set<? extends Class<?>> supers = TypeToken.of(target.getClass()).getTypes().rawTypes();
         Method[] var6 = target.getClass().getMethods();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Method method = var6[var8];
            Iterator var10 = supers.iterator();

            while(var10.hasNext()) {
               Class cls = (Class)var10.next();

               try {
                  Method real = cls.getDeclaredMethod(method.getName(), method.getParameterTypes());
                  if (real.isAnnotationPresent(SubscribeEvent.class)) {
                     Class<?>[] parameterTypes = method.getParameterTypes();
                     Class eventType = parameterTypes[0];

                     try {
                        int busID = (Integer)ReflectionHelper.getPrivateValue(EventBus.class, bus, new String[]{"busID"});
                        Constructor<?> ctr = eventType.getConstructor();
                        ctr.setAccessible(true);
                        Event event = (Event)ctr.newInstance();
                        ASMEventHandler listener = new ASMEventHandler(target, method, owner);
                        event.getListenerList().register(busID, listener.getPriority(), listener);
                        ArrayList<IEventListener> others = (ArrayList)listeners.get(target);
                        if (others == null) {
                           others = new ArrayList();
                           listeners.put(target, others);
                           ReflectionHelper.setPrivateValue(EventBus.class, bus, listeners, new String[]{"listeners"});
                        }

                        others.add(listener);
                     } catch (Exception var20) {
                     }
                     break;
                  }
               } catch (NoSuchMethodException var21) {
               }
            }
         }

      }
   }
}
