# Kafka / Fraud Detection / Wallet



## Message Format
```json
{
  "meta": {
    "message_id": "af5a71f9-4036-43e9-9b22-aec471bd4aff",
    "original_message_id": null,
    "type": "EVENT",
    "service_id": "ronin-wallet",
    "timestamp": 1712052292931,
    "auto_retry": false
  },
  "message_code": "FUND_TRANSFER",
  "payload": {
    "transaction_id": "b1fd55d4-3e8f-440a-8919-b79f2b87bdc3",
    "transaction_type": "PAYMENT",
    "debit_account": "a1",
    "credit_account": "a2",
    "amount": 1000,
    "status": 1,
    "created_at": 1712052292931
  }
}
```


## Test
- cURL:
```shell
curl --location 'http://localhost:8080/api/v1/payments' \
--header 'Content-Type: application/json' \
--data '{
    "debit_account": "A1",
    "credit_account": "A2",
    "amount": 1000
}'
```