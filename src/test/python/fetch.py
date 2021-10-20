from kafka import KafkaConsumer
from json import loads
import base64

# 'prediction-event',
# 'prediction-response',

# TODO Remove all Python tests

consumer = KafkaConsumer(
    'prediction-response',
    bootstrap_servers=['localhost:9092'],
    auto_offset_reset='earliest',
    enable_auto_commit=True,
    group_id='python-test',
    value_deserializer=lambda x: loads(x))
    # value_deserializer=lambda x: loads(base64.b64decode(x)))

for message in consumer:
  message = message.value
  print(message)