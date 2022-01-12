# DisasterAlerter
This is an android application for alerting people by some govt boy by pushing notifications in users' device. This app is made for AtlasHackathon contest using MongoDb Atlas, Triggers, Realm function and FCM.

### Screenshots
| Images      | Images |
| ----------- | ----------- |
| ![!st page](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/059gk2xkx5oa9iy53xe4.jpg)      | ![Admin page](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/up4cptok7jdiw74f6ram.jpg)       |
| ![User page](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/4k3hyr9u6cwmug3ne839.jpg)   | ![Notification](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/l7lvweco8etaxkt8127b.jpg)        |

### Realm function

```
exports = function(changeEvent) {
const cred = {
 // FCM credentials
};
var admin = require("firebase-admin");
admin.initializeApp({
    credential: admin.credential.cert(cred)
});
var alertData = changeEvent.fullDocument;

const topic = '/topics/dis-topic';

    const message = {
        notification: {
            title: alertData.type + ' alert at ' + alertData.time,
            body: alertData.msg
        },
        topic: topic
    };
    return admin.messaging().send(message)
        .then((response) => {

            console.log('Successfully sent message:', response);
        })
        .catch((error) => {
            console.log('Error sending message:', error);
        });
};
```

### Used Technology
In this android application , I have used the technologies given below - 

- [Kotlin](https://kotlinlang.org):
In this project kotlin is used to code the full application on Android Studio
- [Mongodb-atlas Database](https://www.mongodb.com/atlas/database):
Mongodb-atlas database is used to store all the data admin send for alert
- [Mongodb Data-Api](https://docs.atlas.mongodb.com/api/data-api):
Data-Api is used to read and write data from Mongodb Database in the android application through network call
- [Mongodb Triggers](https://docs.atlas.mongodb.com/triggers):
When a new document is created via Data-Api, a Realm Function is called to send a request in [FCM](https://firebase.google.com/docs/cloud-messaging) to send notifications to user end.
- [FCM](https://firebase.google.com/docs/cloud-messaging):
Firebase Cloud Messaging is used to send topic wise notifications using Realm function.
