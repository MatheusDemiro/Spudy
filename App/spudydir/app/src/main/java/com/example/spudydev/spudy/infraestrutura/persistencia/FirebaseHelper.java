package com.example.spudydev.spudy.infraestrutura.persistencia;

import com.example.spudydev.spudy.infraestrutura.MpooAppException;
import com.example.spudydev.spudy.infraestrutura.MpooAppRuntimeException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Firebase criado com regras de leitura e escrita públicas.
 *
 *
 * Created by gabri on 23/01/2018.
 */
public class FirebaseHelper {
    public static final FirebaseHelper instance = new FirebaseHelper();

    public DatabaseReference getDatabaseReference() {
        return AcessoFirebase.getFirebase();
    }

    public <T extends Object> List<T> getAll(Query query, final Class<T> aClass) throws MpooAppException {
        MpooFirebaseSyncQuery<T> syncQuery = new MpooFirebaseSyncQuery<>(query, aClass);
        return syncQuery.getResult();
    }

    public <T extends Object> T getFirst(Query query, final Class<T> aClass) throws MpooAppException {
        MpooFirebaseSyncQuery<T> syncQuery = new MpooFirebaseSyncQuery<>(query, aClass, 1);
        return syncQuery.getFirstResult();
    }

    private static class MpooFirebaseSyncQuery<T> implements ValueEventListener {
        private final Query query;
        private final TaskCompletionSource<List<T>> taskCompletionSource = new TaskCompletionSource<>();
        private final Class<T> aClass;
        private int limit = -1;
        MpooFirebaseSyncQuery(Query query, Class<T> aClass) {
            this(query, aClass, -1);
        }

        MpooFirebaseSyncQuery(Query query, Class<T> aClass, int limit) {
            this.query = query;
            this.aClass = aClass;
            this.limit = limit;
        }

        public List<T> getResult() throws MpooAppException {
            List<T> result;
            this.query.addValueEventListener(this);
            Task<List<T>> task = taskCompletionSource.getTask();
            waitTaskCompletion(task);
            this.query.removeEventListener(this);

            Exception exception = task.getException();
            if (exception != null) {
                throw new MpooAppRuntimeException("Erro ao consultar os dados", exception);
            }
            result = task.getResult();
            return result;
        }

        public T getFirstResult() throws MpooAppException {
            T result = null;
            List<T> ts = getResult();
            if (!ts.isEmpty()) {
                result = ts.get(0);
            }
            return result;
        }

        private void waitTaskCompletion(Task<List<T>> task) throws MpooAppException {
            long timeout = 10000;
            while (!task.isComplete()) {
                try {
                    long sleep = 500;
                    Thread.sleep(sleep);
                    timeout -= sleep;
                    if (timeout <= 0) {
                        throw new MpooAppException("Tempo máximo de conexão excedido");
                    }
                } catch (InterruptedException e) {
                    //do nothing
                }
            }
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<T> ts = new ArrayList<>();
            int valuesLeft = this.limit;
            for (DataSnapshot tSnapshot : dataSnapshot.getChildren()) {
                if (valuesLeft == 0) {
                    break;
                }
                T t = tSnapshot.getValue(aClass);
                ts.add(t);
                valuesLeft--;
            }
            taskCompletionSource.setResult(ts);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            taskCompletionSource.setException(databaseError.toException());
        }
    };
}