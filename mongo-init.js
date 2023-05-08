db = db.getSiblingDB('meat_store')

db.createUser({
  user: 'danila',
  pwd: 'bratushka',
  roles: [
    {
      role: 'readWrite',
      db: 'meat_store',
    },
  ],
});

db.coordinates.insert({ x: 1, y: 1 })
