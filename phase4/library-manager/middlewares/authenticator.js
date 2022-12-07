const jwt = require('jsonwebtoken');

module.exports = (req, res, next) => {
  if (!req.headers.authorization) {
    return res.status(403).json({ error: 'HTTP Authorization Header is empty.' });
  }

  const token = req.headers.authorization.split(' ')[1];
  if (!token) {
    return res.status(403).json({ error: 'Bearer token is empty.' });
  }

  let payload;
  try {
    payload = jwt.verify(token, req.app.get('jwtsecret'));
  } catch (err) {
    return res.status(403).json({ error: 'Failed to verify token: ' + err + "\n" });
  }

  req.jwtPayload = payload;
  next();
}
