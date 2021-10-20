from time import sleep
from json import dumps
from kafka import KafkaProducer

producer = KafkaProducer(bootstrap_servers=['localhost:9092'],
                         value_serializer=lambda x:
                         dumps(x).encode('utf-8'))

for e in range(1):
  data = {
    "features":{
      "pickup_location_id": 75,
      "pickup_hour_of_day": 7,
      "pickup_day_of_week": 2,
      "pickup_week_of_year": 50,
      "passenger_count": e
    }
  }

  producer.send('prediction-request', value=data)
  sleep(1)